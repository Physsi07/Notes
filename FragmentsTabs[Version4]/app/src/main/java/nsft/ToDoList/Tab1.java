package nsft.ToDoList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.app.*;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {

    private static final String TAG = "Tab1";

    // OBJECTS FROM OTHER CLASSES //
    DBHelper dbHelper;

    // VARIABLES //
    ArrayAdapter<String> mAdapter;
    ListView task;
    ArrayList<String> list;

    ImageView imgC;


    public Tab1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // INFLATING A VIEW VARIABLE //
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        // ADDING THOSE ITEMS INTO THE LIST //
        setHasOptionsMenu(true);

        imgC = view.findViewById(R.id.checked);

        // INSTANTIATE OF THE DBHELPER //
        dbHelper = new DBHelper(getActivity());

        // GET ALL THE ID OF ALL MY XML ITEMS //
        task = (ListView) view.findViewById(R.id.list1);

        // INITIALIZING THE ARRAY LIST //
        list = new ArrayList<String>();

        // SETTING THE ADAPTER //
        mAdapter = new ArrayAdapter<String>(getContext(), R.layout.table_row, R.id.textView, list);

        // LOAD THE ARRAY LIST WHEN THE APPLICATION STARS ALSO //
        loadTaskList();

        // DELETE FUNCTION //
        deleteTask();

        task.setAdapter(mAdapter);
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
                        . setTitle("Task")

                        // SETS THE MESSAGE
                        .setMessage("Personal Task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // STRING VARIABLE //
                                String personalTask = String.valueOf(taskEditText.getText());

                                // DEBUGGING //
                                Log.d("Tab1", "INSERTING PERSONAL TASK " + personalTask);

                                // INSERTING INTO THE NEXT ROW THATS EMPTY //
                                dbHelper.insertToPersonal(personalTask);

                                loadTaskList();

                                // DEBUGGING //
                                Log.d("Tab1", "LOADING PERSONAL TASK " + personalTask);

                                // TELLING THE USER IN A TOAST THAT THE TASK WAS ADDED
                                Toast.makeText(getActivity(), "PERSONAL TASK ADDED", Toast.LENGTH_SHORT).show();
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
        ArrayList<String> taskList = dbHelper.loadPersonalList();

        // CHECKING IF THE ADAPTER IS NULL //
        if (mAdapter == null) {
            Log.d("Tab1", "PERSONAL ADAPTER IS NULL");

            // IF IT IS NULL, SET A NEW ADAPTER WITH NEW VALUES //
            mAdapter = new ArrayAdapter<String>(getContext(), R.layout.table_row, R.id.textView, taskList);

            // SETTING THE ADAPTER //
            task.setAdapter(mAdapter);
        }
        // OR WE JUST WILL CLEAR THE ADAPTER AND ...  //
        else{
            Log.d("Tab1", "PERSONAL ADAPTER IS GOOD");

            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
        dbHelper.close();
    }

    private void deleteTask(){

        task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                Toast.makeText(getContext(), "Position clicked " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // SETTING THE LIST WHEN IS HOLD TO BE DELETED //
        task.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    AlertDialog dialog = new AlertDialog.Builder( getActivity() )

                            .setTitle("Are you sure you want to delete")
                            .setMessage("Deleting")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // DELETE THE ITEM //
                                    dbHelper.deleteFromPersonal(list.get(position));
                                    list.remove(position);

                                    // NOTIFY CHANGES TO THE ADAPTER //
                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "TASK CLICKED: " + position, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                return true;
            }
        });

        task.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
