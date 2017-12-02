package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton FAB;
    List<Task> taskArrayList = new ArrayList<>();
    ListView listView;
    MyTaskListAdapter adapter;
    Toolbar myToolbar;
    boolean reversedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        FAB = (FloatingActionButton) findViewById(R.id.FAB);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NewTaskActivity.class);
                startActivity(i);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyList));
        adapter = new MyTaskListAdapter(this, taskArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskId = (Integer)view.getTag();
                Intent i = new Intent(view.getContext(), TaskActivity.class);
                i.putExtra("taskSelected", taskId);
                startActivity(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, !view.isActivated());
                return true;
            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            int counter = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    counter++;
                    adapter.setNewSelection(position, true);
                } else {
                    counter--;
                    adapter.removeSelection(position);
                }
                if (counter > 1) {
                    mode.getMenu().findItem(R.id.edit).setVisible(false);
                } else {
                    mode.getMenu().findItem(R.id.edit).setVisible(true);
                }
                mode.setTitle(String.valueOf(counter));

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                final SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                switch (item.getItemId()) {
                    case R.id.delete:
                        String taskPlural;
                        if(counter > 1) {
                        taskPlural = "tasks?";
                        } else {
                            taskPlural = "task?";
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Delete this " + String.valueOf(counter) + taskPlural)
                                .setPositiveButton(R.string.delete_caps, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        for (int i = listView.getAdapter().getCount()-1; i >= 0; i--) {
                                            if (checkedItems.get(i)) {
                                                taskArrayList.remove(i);
                                                adapter.remove(adapter.getItem(i));
                                                adapter.clearSelection();
                                                adapter.notifyDataSetChanged();

                                                GsonBuilder builder = new GsonBuilder()
                                                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
                                                Gson gson = builder.create();

                                                SharedPreferences prefs = getSharedPreferences(
                                                        "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefs.edit();
                                                String json = gson.toJson(taskArrayList);
                                                editor.putString("taskList", json);
                                                editor.apply();
                                            }
                                        }
                                        mode.finish();
                                    }

                                })
                                .setNegativeButton(R.string.cancel_caps, null)
                                .show();
                        return true;
                    case R.id.edit:
                        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                            if (checkedItems.get(i)) {
                                Integer taskId = (Integer)(getViewByPosition(i, listView)).getTag();
                                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                                intent.putExtra("taskId", taskId);
                                mode.finish();
                                startActivity(intent);

                            }
                        }
                    default:
                        return false;
                }
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelection();
                counter = 0;
            }
        });

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.swap:
                reversedList ^= true;
                Collections.reverse(taskArrayList);
                adapter.clear();
                adapter.addAll(taskArrayList);
                adapter.notifyDataSetChanged();
                GsonBuilder builder = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
                Gson gson = builder.create();

                SharedPreferences prefs = getSharedPreferences(
                        "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);

                String json = gson.toJson(taskArrayList);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("taskList", json);
                editor.putBoolean("reversedList", reversedList);
                editor.apply();
                return true;
            case R.id.sort_by:
                Intent i1 = new Intent(this, SortByActivity.class);
                startActivity(i1);
                return true;
            case R.id.settings:
                Intent i2 = new Intent(this, SettingsActivity.class);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = builder.create();

        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);

        String jsonA = prefs.getString("taskList", null);
        if (jsonA == null) {
            taskArrayList = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            taskArrayList = gson.fromJson(jsonA, type);
        }

        String sortingMethod = prefs.getString("sortingMethod", "CTVDueDate");
        switch (sortingMethod) {
            case "CTVDueDate":
                Collections.sort(taskArrayList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getDueDate().compareTo(o2.getDueDate());
                    }
                });
                break;
            case "CTVTitle":
                Collections.sort(taskArrayList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }
                });
                break;
            case "CTVCreationDate":
                Collections.sort(taskArrayList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getCreationDate().compareTo(o2.getCreationDate());
                    }
                });
                break;
            case "CTVPriority":
                Collections.sort(taskArrayList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o2.getPriority() - o1.getPriority();
                    }
                });
                break;
        }
        reversedList = prefs.getBoolean("reversedList", false);
        if (reversedList) {
            Collections.reverse(taskArrayList);
        }

        jsonA = gson.toJson(taskArrayList);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("taskList", jsonA);
        editor.apply();

        adapter.clear();
        adapter.addAll(taskArrayList);
        adapter.notifyDataSetChanged();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                myToolbar.showOverflowMenu();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

}