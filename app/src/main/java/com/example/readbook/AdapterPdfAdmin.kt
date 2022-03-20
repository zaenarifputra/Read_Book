package com.example.readbook

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.readbook.databinding.RowPdfAdminBinding

class AdapterPdfAdmin :RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin>, Filterable{

    //context
    private var context: Context
    //arraylist to hold pdfs
    public var pdfArrayList: ArrayList<ModelPdf>
    private val filterlist:ArrayList<ModelPdf>

    //viewBinding
    private lateinit var binding:RowPdfAdminBinding

    //filter object
    private var filter: FilterPdfAdmin? = null

    //constructor
    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterlist = pdfArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        //bind/inflate layout row_pdf_admin.xml
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderPdfAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        /*----Get Data, Set Data, Handle click etc----*/

        //get data
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val pdfurl = model.url
        val timestamp = model.timestamp
        //convert timestamp to dd/MM/yyyy format
        val formattedDate = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = formattedDate

        //load further details like category, pdf from url, pdf size

        //load category id
        MyApplication.loadCategory(categoryId, holder.categoryTv)

        //we don't need page number here, pas null for page number //load pdf thumbnail
        MyApplication.loadPdfFromUrlSinglePage(pdfurl, title, holder.pdfView, holder.progressBar, null)

        //load pdf size
        MyApplication.loadPdfSize(pdfurl, title, holder.sizeTv)

        //handle click, show dialog with options 1) Edit Book, 2) Delete Book
        holder.moreBtn.setOnClickListener {
            moreOptionsDialog(model, holder)
        }

    }

    private fun moreOptionsDialog(model: ModelPdf, holder: AdapterPdfAdmin.HolderPdfAdmin) {
        //get id,url,title of book
        val bookId = model.id
        val bookUrl = model.url
        val bookTitle = model.title

        //options to show in dialog
        val options = arrayOf("Edit", "Delete")

        //alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){dialog, position->
                //handle item click
                if (position==0){
                    //Edit is clicked
                    val intent = Intent(context, PdfEditActivity::class.java)
                    intent.putExtra("bookId", bookId) //passed bookId, will be used to edit the book
                    context.startActivity(intent)
                }
                else if (position == 1){
                    //Delete is clicked

                    //show confirmation dialog first
                    MyApplication.deleteBook(context, bookId, bookUrl, bookTitle)
                }
            }
            .show()

    }

    override fun getItemCount(): Int {
        return pdfArrayList.size //items count
    }


    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfAdmin(filterlist, this)
        }

        return filter as FilterPdfAdmin
    }


    /*View Holder class for row_pdf_admin.xml*/
    inner class HolderPdfAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){
        //UI Views of row_pdf_admin.xml
        val pdfView = binding.pdfView
        val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv
        val moreBtn = binding.moreBtn

    }
}