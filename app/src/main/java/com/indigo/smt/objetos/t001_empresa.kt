package com.indigo.smt.objetos

class t001_empresa {

    var f001_id:String?=null
    var f001_nombre:String?=null
    var f001_logo:String?=null
    var f001_estado:String?=null

    constructor(){}

    constructor(f001_id_:String,f001_nombre_:String,f001_logo_:String,f001_estado_:String){

        this.f001_id = f001_id_
        this.f001_nombre = f001_nombre_
        this.f001_logo = f001_logo_
        this.f001_estado = f001_estado_

    }

}