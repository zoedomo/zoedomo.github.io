package com.three19.todolistmod5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.three19.todolistmod5.database.ToDoListDB;
import com.three19.todolistmod5.model.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ToDoListDB toDoListDB;
    List<ToDo> arrayList;
    ToDoListAdapter adapter;
    ToDo selectedToDo;
    int selectedPosition;
    EditText txtName;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (EditText) findViewById(R.id.txtName);

        toDoListDB = new ToDoListDB(this);
        arrayList = toDoListDB.getList();

        adapter = new ToDoListAdapter(this, (ArrayList<ToDo>) arrayList);

        ListView listView = (ListView) findViewById(R.id.lstView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                removeItemFromList(position);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedToDo = arrayList.get(position);
                selectedPosition = position;
                txtName.setText(selectedToDo.getName());

                addBtn.setText("Update");
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = txtName.getText().toString();
                        selectedToDo.setName(name);
                        toDoListDB.update(selectedToDo);
                        arrayList.set(selectedPosition, selectedToDo);
                        adapter.notifyDataSetChanged();
                        reset();
                    }
                });

            }
        });

        addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = txtName.getText().toString();

                if (selectedToDo == null) {
                    ToDo newToDo = new ToDo();
                    newToDo.setName(name);
                    toDoListDB.add(name);
                    arrayList.add(newToDo);
                    adapter.notifyDataSetChanged();
                } else {
                    selectedToDo.setName(name);
                    toDoListDB.update(selectedToDo);
                    arrayList.set(selectedPosition, selectedToDo);
                    adapter.notifyDataSetChanged();
                    reset();
                }
            }
        });

        Button clearBtn = (Button) findViewById(R.id.btnClear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });

        Button allBtn = (Button) findViewById(R.id.btnAll);
        allBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AllTasksActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void removeItemFromList(final int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToDo toDo = arrayList.get(position);

                arrayList.remove(position);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

                toDoListDB.remove(toDo.getId());
                reset();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    protected void reset() {
        txtName.setText("");

        addBtn.setText("Add");

        selectedToDo = null;
        selectedPosition = -1;
    }
}

class ToDoListAdapter extends ArrayAdapter<ToDo>
{
    public ToDoListAdapter(Context context, ArrayList<ToDo> toDoList) {
        super(context, 0, toDoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDo toDo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(android.R.id.text1);
        name.setText(toDo.getName());

        return convertView;
    }
}