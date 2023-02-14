package com.indigo.smt.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.indigo.smt.R
import com.indigo.smt.objetos.t004_banners
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter (ListimageDecode: ArrayList<t004_banners>) :

    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    // on below line we are creating a
    // new array list and initializing it.
    var sliderList: ArrayList<t004_banners> = ListimageDecode

    // on below line we are calling get method
    override fun getCount(): Int {
        // in this method we are returning
        // the size of our slider list.
        return sliderList.size
    }

    // on below line we are calling on create view holder method.
    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        // inside this method we are inflating our layout file for our slider view.
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item, null)

        // on below line we are simply passing
        // the view to our slider view holder.
        return SliderViewHolder(inflate)
    }

    // on below line we are calling on bind view holder method to set the data to our image view.
    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {

        // on below line we are checking if the view holder is null or not.
        if (viewHolder != null) {
            // if view holder is not null we are simply
            // loading the image inside our image view using glide library
            var imagen = sliderList.get(position).f004_imagen.toString()

            var byteArray = Base64.decode(imagen, Base64.DEFAULT)
            var bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

            Glide.with(viewHolder.itemView).load(bmp).fitCenter()
                .into(viewHolder.slideBanner)


        }
    }

    // on below line we are creating a class for slider view holder.
    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {

        // on below line we are creating a variable for our
        // image view and initializing it with image id.
        var slideBanner: ImageView = itemView!!.findViewById(R.id.slideBanner)
    }
}