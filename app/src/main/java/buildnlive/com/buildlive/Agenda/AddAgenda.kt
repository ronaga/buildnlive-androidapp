/*
package buildnlive.com.buildlive.Agenda

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.utils.UtilityofActivity
import kotlinx.android.synthetic.main.toolbar.*


class AddAgenda : AppCompatActivity() {

    private var context: Context? = null
    private var appCompatActivity: AppCompatActivity? = this
    var utilityofActivity: UtilityofActivity? = null
    var title: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_add_agenda)

        context = this
        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        utilityofActivity!!.configureToolbar(appCompatActivity!!)
        toolbar_subtitle.text = getString(R.string.create_new_agenda)

        notification.visibility = View.GONE
        addItem.visibility = View.GONE

*/
/*
        val data = "{\n" +
                "  \"success\": true,\n" +
                "  \"message\": \"Success\",\n" +
                "  \"layer\": \"0\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"catId\": \"1\",\n" +
                "      \"name\": \"Sports\",\n" +
                "      \"subcategories\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"catId\": \"2\",\n" +
                "      \"name\": \"Home\",\n" +
                "      \"subcategories\": true\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        val json = Gson()

        val spinnerData1 = json.fromJson(data, CategorySpinnerResponse::class.java).data
        spinnerData1.add(0, CategorySpinnerResponseData("0", "Select Category", false))
        customSpinnerAdapter = CustomSpinnerAdapter(context!!, R.layout.custom_spinner, spinnerData1)
        categorySpinner.adapter = customSpinnerAdapter


        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                if (position > 0) {
                    if (customSpinnerAdapter!!.getSubcategoriesStatus(position)) {

                        val data2 = "{\n" +
                                "  \"success\": true,\n" +
                                "  \"message\": \"Success\",\n" +
                                "  \"layer\": \"1\",\n" +
                                "  \"data\": [\n" +
                                "    {\n" +
                                "      \"catId\": \"3\",\n" +
                                "      \"name\": \"Basketball\",\n" +
                                "      \"subcategories\": false\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"catId\": \"4\",\n" +
                                "      \"name\": \"Food\",\n" +
                                "      \"subcategories\": true\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"
                        val json2 = Gson()
                        val spinnerData2 = json2.fromJson(data2, CategorySpinnerResponse::class.java).data
                        spinnerData2.add(0, CategorySpinnerResponseData("0", "Select Subcategory", false))
                        addSpinner(spinnerData2)
                        Logger.e("ChildCount On Add Spinner Click", insertPoint!!.childCount.toString())
                    } else {
                        Logger.e("ChildCount On First Spinner Click", insertPoint!!.childCount.toString())
                        if (insertPoint!!.childCount > 1) {
                            insertPoint!!.removeViewAt(1)
                        }
                    }
                }
            }

        }*//*



    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}



*/
