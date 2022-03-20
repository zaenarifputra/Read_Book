package com.example.readbook

import android.widget.Filter

class FilterPdfAdmin : Filter {
    //arraylist in which we want to seacrh
    var filterList: ArrayList<ModelPdf>

    //adapter in which filter need to be implemented
    var adapterPdfAdmin: AdapterPdfAdmin

    //constructor
    constructor(filterList: ArrayList<ModelPdf>, adapterPdfAdmin: AdapterPdfAdmin) {
        this.filterList = filterList
        this.adapterPdfAdmin = adapterPdfAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint:CharSequence? = constraint //value to search
        val results = FilterResults()
        //value to be searched should not be null and not empty
        if (constraint != null && constraint.isNotEmpty()){
            //change to upper case, or lowercase to avoid case sensitivity
            constraint =constraint.toString().lowercase()
            var filteredModels = ArrayList<ModelPdf>()
            for (i in filterList.indices){
                //validate if match
                if (filterList[i].title.lowercase().contains(constraint)){
                    //searched value is similar to value in list, add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            results.count = filterList.size
            results.values = filterList
        }

        return results // don't miss
    }

    override fun publishResults(constarint: CharSequence, results: FilterResults) {
         adapterPdfAdmin.pdfArrayList = results.values as ArrayList<ModelPdf>

        adapterPdfAdmin.notifyDataSetChanged()
    }


}