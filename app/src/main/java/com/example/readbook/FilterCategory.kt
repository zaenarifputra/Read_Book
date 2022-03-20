package com.example.readbook

import android.widget.Filter

class FilterCategory: Filter {

    //arraylist in which we want to search
    private var filterList: ArrayList<ModelCategory>

    //adapter in which filter need to be implemented
    private var adapterCategory: AdapterCategory

    //constructor
    constructor(filterList: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) : super() {
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val result = FilterResults()

        //value should not be null and not empty
        if (constraint != null && constraint.isEmpty()){
            //searched value is nor null not empty

            //change to upper case, or lower case to avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels:ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size){
                //validate
                if (filterList[i].category.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            result.count = filteredModels.size
            result.values = filteredModels

        }
        else{
            //search value is either null or empty
            result.count = filterList.size
            result.values = filterList
        }

        return result //don't miss it
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategory>

        //notify changes
        adapterCategory.notifyDataSetChanged()
    }


}