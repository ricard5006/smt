package com.indigo.smt

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.indigo.smt.dbHelper.DbHelper
import com.indigo.smt.objetos.t001_empresa
import com.indigo.smt.objetos.t002_usuarios
import org.json.JSONArray
import org.json.JSONObject


class login : AppCompatActivity() {

    private lateinit var imgLogo : ImageView
    private lateinit var tbLogin : EditText
    private lateinit var tbNickname : EditText
    private lateinit var btnIngresar : Button
    private lateinit var tbCerrarSesion : TextView

    private lateinit var layoutUpdate : LinearLayout

    private lateinit var servidor:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        imgLogo = findViewById(R.id.imgLogo)

        tbLogin = findViewById(R.id.tbLogin)
        tbNickname = findViewById(R.id.tbNickname)
        btnIngresar = findViewById(R.id.btnIngresar)
        tbCerrarSesion = findViewById(R.id.tbCerrarSesion)

        layoutUpdate = findViewById(R.id.layoutUpdate)

        servidor = "https://smt.etimarcas.com"
        //servidor = "http://192.168.20.2/smt" //local

        layoutUpdate.visibility = View.VISIBLE

        get_t001_empresa_service()

        get_t002_usuario_local()

        btnIngresar.setOnClickListener {
            validarUsuario_service(tbLogin.text.toString(),tbNickname.text.toString())
        }

        tbCerrarSesion.setOnClickListener {
            cerrarSesion()
        }


    }

    private fun get_t001_empresa_local(){
        val objdbhelper = DbHelper(this)
        val objempresa = objdbhelper.select_t001_empresa()

        if(objempresa.f001_logo.toString()!="" && objempresa.f001_logo != null){

        var byteArray = Base64.decode(objempresa.f001_logo,Base64.DEFAULT)

        var bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

        imgLogo.setImageBitmap(bmp)

        layoutUpdate.visibility = View.GONE

        }else{
            layoutUpdate.visibility = View.VISIBLE
        }
    }

    private fun set_t001_empresa_local(obj_t001_empresa:t001_empresa){
        val objdbhelper = DbHelper(this)
        objdbhelper.insert_t001_empresa(obj_t001_empresa)
    }

    private fun get_t001_empresa_service(){
        val queue = Volley.newRequestQueue(this)
        var obj_empresa = t001_empresa()
        var url = "$servidor/getlogo.php"

        //var url1 = "https://smt.etimarcas.com/getlogo.php"

        val stringRequest = StringRequest(Request.Method.GET,url, Response.Listener { response ->

            if(response != null && response != "[]"){
                //si responde con informacion se muestra el logo y lo almacena localmente.

                val jsonArray = JSONArray(response)

                val jsonObj = JSONObject(jsonArray.getString(0))

                var dataEncode= jsonObj.get("f001_logo").toString()

                var byteArray = Base64.decode(dataEncode,Base64.DEFAULT)

                var bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

                imgLogo.setImageBitmap(bmp)

                obj_empresa.f001_nombre = jsonObj.get("f001_nombre").toString()
                obj_empresa.f001_logo = jsonObj.get("f001_logo").toString()
                set_t001_empresa_local(obj_empresa)

                layoutUpdate.visibility = View.GONE

            }else{

                get_t001_empresa_local()

            }







        },Response.ErrorListener { error ->
            get_t001_empresa_local()
            Toast.makeText(this@login,"Lo sentimos en este momento no podemos sincronizar.",Toast.LENGTH_LONG).show()
        })

        queue.add(stringRequest)

    }

    private fun set_t002_usuario(obj_t002_usuario:t002_usuarios){
        val objdbhelper = DbHelper(this)
        objdbhelper.insert_t002_usuarios(obj_t002_usuario)


    }

    private fun get_t002_usuario_local(){
        val objdbhelper = DbHelper(this)
        val objUsuario = objdbhelper.select_t002_usuarios()

        if(objUsuario.f002_login.toString() != "" && objUsuario.f002_login !=null){
            tbLogin.setText(objUsuario.f002_login.toString())
            tbNickname.setText(objUsuario.f002_nickname.toString())

            tbLogin.isEnabled = false
            tbNickname.isEnabled = false

        }



    }

    private fun validarUsuario_service(login: String, nickname: String) {
        val obj_t002_usuario = t002_usuarios()

        if(login == "" || nickname == ""){
            Toast.makeText(this,"Por favor ingresar los datos completos",Toast.LENGTH_SHORT).show()

        }else{

            val queue = Volley.newRequestQueue(this)

            var url = "$servidor/getusuario.php"

            val stringRequest = object : StringRequest(Request.Method.POST,url,
            object:Response.Listener<String?>
            {
                override fun onResponse(response: String?) {

                    if (response != null && response != "[]") {

                        val jsonArray = JSONArray(response)

                        val jsonObj = JSONObject(jsonArray.getString(0))

                        obj_t002_usuario.f002_login = jsonObj.get("f002_login").toString()
                        obj_t002_usuario.f002_nickname = jsonObj.get("f002_nickname").toString()
                        obj_t002_usuario.f002_estado = jsonObj.get("f002_estado").toString()

                        if(obj_t002_usuario.f002_login == login && obj_t002_usuario.f002_nickname == nickname){
                            if(obj_t002_usuario.f002_estado == "1"){
                                Toast.makeText(this@login,"Acceso",Toast.LENGTH_SHORT).show()
                                set_t002_usuario(obj_t002_usuario)
                                //Abrir nuevo activity
                                val intent = Intent(this@login,MainActivity::class.java)
                                startActivity(intent)

                            }else{
                                Toast.makeText(this@login,"Usuario inactivo",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@login,"Usuario no existe",Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(this@login,"Usuario no existe",Toast.LENGTH_SHORT).show()
                    }

                }



            },
            object:Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@login,"Sin respuesta!",Toast.LENGTH_SHORT).show()
                }

            })
            {


                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("login","$login")
                    params.put("nickname","$nickname")
                    return params
                }


            }
            queue.cache.clear()
            queue.add(stringRequest)

            }






            }

    private fun cerrarSesion(){
        val obj_t002_usuario = t002_usuarios()
        obj_t002_usuario.f002_nickname = ""
        obj_t002_usuario.f002_login = ""
        obj_t002_usuario.f002_estado = ""
        tbLogin.isEnabled = true
        tbNickname.isEnabled = true
        tbLogin.setText("")
        tbNickname.setText("")
        set_t002_usuario(obj_t002_usuario)
        Toast.makeText(this@login,"Cerrando sesion!",Toast.LENGTH_SHORT).show()
    }


}