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
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, list2);

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
                                String task = String.valueOf(taskEditText.getText());

                                // DEBUGGING //
                                Log.d("Tab2", "INSERTING BUSINESS TASK " + task);

                                // INSERTING INTO THE NEXT ROW THATS EMPTY //
                                dbHelper.insertTask(task);

                                // LOADING THE LIST BACK T THE LIST VIEW //
                                loadTaskList();

                                // DEBUGGING //
                                Log.d("Tab2", "LOADING BUSINESS TASK " + task);

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
        ArrayList<String> taskList = dbHelper.getTaskList();

        // CHECKING IF THE ADAPTER IS NULL //
        if (mAdapter == null) {
            Log.d("Tab2", "BUSINESS ADAPTER IS NULL");

            // IF IT IS NULL, SET A NEW ADAPTER WITH NEW VALUES //
            mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, list2);

            // SETTING THE ADAPTER //
            task2.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        // OR WE JUST WILL CLEAR THE ADAPTER AND ...  //
        else{
            Log.d("Tab2", "BUSINESS ADAPTER IS GOOD");
            String value = taskList.get(taskList.size() - 1);
            mAdapter.add(String.valueOf(value));

            if (list2.isEmpty()){
                Log.d("Tab2", "BUSINESS LIST IS EMPTY");
                mAdapter.add(String.valueOf(task2));
            }
            else{
                Log.d("Tab2", "BUSINESS LIST HAS TASKS");

            }

        }
    }

    private void deleteTask(){

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
                                list2.remove(position);

                                mAdapter.notifyDataSetChanged();

                                // SHOWING A TOAST TO THE USER //
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
    }
}