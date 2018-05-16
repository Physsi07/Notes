package nsft.ToDoList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.View.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment {

    // OBJECTS FROM OTHER CLASSES //
    DBHelper dbHelper;

    // VARIABLES //
    ArrayAdapter<String> mAdapter;
    ListView task2;
    ArrayList<String> list2;

    //
    public Tab2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // INFLATING A VIEW VARIABLE //
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        // ADDING THOSE ITEMS INTO THE LIST //
        setHasOptionsMenu(true);

        // INSTANTIATE OF THE DBHELPER //
        dbHelper = new DBHelper(getActivity());

        // GET ALL THE ID OF ALL MY XML ITEMS //
        task2 = (ListView) view.findViewById(R.id.list2);

        // INITIALIZING THE ARRAY LIST //
        list2 = new ArrayList<String>();

        // SETTING THE ADAPTER //
        mAdapter = new ArrayAdapter<String>(getContext(), R.layout.table_row, R.id.textView, list2);

        // LOAD THE ARRAY LIST WHEN THE APPLICATION STARS ALSO //
        loadTaskList();

        // DELETE FUNCTION //
        deleteTask();

        task2.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // RETURN VIEW //
        return view;
    }



    public interface OnFragmentInteractionListener {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // INSERTING THE MENU ITEMS TO THE TOOLBAR //
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        // CHECKS WHAT ITEM FOR THE MENU THAT are in the toolbar was selected //
        switch (item.getItemId()) {
            case R.id.plus_btn:

                // TOAST FOR THE USER //
                Toast.makeText(getActivity(), "ADD BUTTON CLICKED", Toast.LENGTH_SHORT).show();

                final EditText taskEditText = new EditText(getActivity());
                AlertDialog dialog = new AlertDialog.Builder(getActivity())

                        // SETS THE TITLE FOR THE ALERT DIALOG BOX //
                        .setTitle("Task")

                        // SETS THE MESSAGE
                        .setMessage("Business Task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // STRING VARIABLE //
                                String businessTask = String.valueOf(taskEditText.getText());

                                // DEBUGGING //
                                Log.d("Tab2", "INSERTING BUSINESS TASK " + businessTask);

                                // INSERTING INTO THE NEXT ROW THATS EMPTY //
                                dbHelper.insertToBusiness(businessTask);

                                loadTaskList();

                                // DEBUGGING //
                                Log.d("Tab2", "LOADING BUSINESS TASK " + businessTask);

                                // TELLING THE USER IN A TOAST THAT THE TASK WAS ADDED
                                Toast.makeText(getActivity(), "BUSINESS TASK ADDED", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTaskList() {

        // CREATING AN ARRAY LIST OF TASK OR LIST ITEMS AND STORING IT INTO THE LIST OF THE DATABASE //
        ArrayList<String> taskList = dbHelper.loadBusinessList();

        // CHECKING IF THE ADAPTER IS NULL //
        if (mAdapter == null) {
            Log.d("Tab2", "BUSINESS ADAPTER IS NULL");

            // IF IT IS NULL, SET A NEW ADAPTER WITH NEW VALUES //
            mAdapter = new ArrayAdapter<String>(getContext(), R.layout.table_row, R.id.textView , taskList);

            // SETTING THE ADAPTER //
            task2.setAdapter(mAdapter);
        }
        // OR WE JUST WILL CLEAR THE ADAPTER AND ...  //
        else{
            Log.d("Tab2", "BUSINESS ADAPTER IS GOOD");

            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void deleteTask(){

        task2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                Toast.makeText(getContext(), "Position clicked " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // SETTING THE LIST WHEN IS HOLD TO BE DELETED //
        task2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Are you sure you want to delete ?")
                        .setMessage("Deleting")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // DELETE THE ITEM //
                                dbHelper.deleteFromBusinessl(list2.get(position));
                                list2.remove(position);

                                // NOTIFY CHANGES TO THE ADAPTER //
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "BUSINESS TASK DELETED", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        task2.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}