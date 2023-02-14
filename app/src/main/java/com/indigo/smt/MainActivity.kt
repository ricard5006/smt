package com.indigo.smt

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.indigo.smt.adapters.SliderAdapter
import com.indigo.smt.objetos.t001_empresa
import com.indigo.smt.objetos.t004_banners
import com.smarteist.autoimageslider.SliderView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var banner : ImageView

    private lateinit var servidor:String

    lateinit var imageUrl: ArrayList<String>
    lateinit var sliderView: SliderView
    lateinit var sliderAdapter: SliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        banner = findViewById(R.id.banner)

        sliderView = findViewById(R.id.slider)




        //servidor = "https://smt.etimarcas.com"
        servidor = "http://192.168.20.2:8081/smt" //local


        get_t001_banner_service()

    }



    private fun get_t001_banner_service(){
        val queue = Volley.newRequestQueue(this)
        val listBanners = ArrayList<t004_banners>()
        var url = "$servidor/getbanner.php"




        val stringRequest = StringRequest(Request.Method.GET,url, Response.Listener { response ->

            if(response != null && response != "[]"){
                //si responde con informacion se muestra el logo y lo almacena localmente.

                val jsonArray = JSONArray(response)

                for(i in 0 until jsonArray.length()){

                    val jsonObj = JSONObject(jsonArray.getString(i))
                    var obj_banners = t004_banners()


                    obj_banners.f004_nombre = jsonObj.get("f004_nombre").toString()
                    obj_banners.f004_imagen = jsonObj.get("f004_imagen").toString()
                    obj_banners.f004_estado = jsonObj.get("f004_estado").toString()

                    listBanners.add(obj_banners)
                }

                /*var dataEncode= jsonObj.get("f004_imagen").toString()
                var byteArray = Base64.decode(dataEncode, Base64.DEFAULT)
                var bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                banner.setImageBitmap(bmp)*/

                cargar_Slide_banners(listBanners)
                //set_t004_banners_local(listBanners)

                //layoutUpdate.visibility = View.GONE

            }else{

                //get_t001_empresa_local()

            }







        }, Response.ErrorListener { error ->
            //get_t001_empresa_local()
            Toast.makeText(this@MainActivity,"Lo sentimos en este momento no podemos sincronizar.", Toast.LENGTH_LONG).show()
        })

        queue.add(stringRequest)

    }

    private fun cargar_Slide_banners(listBanners: ArrayList<t004_banners>) {

       /* imageUrl = ArrayList()

        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdsa-self-paced-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdata-science-live-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Ffull-stack-node-thumbnail.png&w=1920&q=75") as ArrayList<String>
*/
        sliderAdapter = SliderAdapter( listBanners)
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()

    }


}