package criminalintent.android.bignerdranch.com.criminalintent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ScarS on 29.09.2014.
 */
public class NoteListFragment extends ListFragment{
    public static final String TAG="NoteListFragment";
    private ArrayList<Note> mNotes;
    private boolean mSubtitleVisible;

    @Override
    public void onResume() {
        super.onResume();
        ((NoteAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_file_list,menu);
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Note note = new Note();
                NoteLab.get(getActivity()).addCrime(note);
                Intent i = new Intent(getActivity(), NotePagerActivity.class);
                i.putExtra(NoteFragment.EXTRA_NOTE_ID, note.getId());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                if(getActivity().getActionBar().getSubtitle()==null){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible=true;
                    item.setTitle(R.string.hide_subtitle);
                }else {
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible=false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position=info.position;
        NoteAdapter adapter = (NoteAdapter) getListAdapter();
        Note note = adapter.getItem(position);

        switch(item.getItemId()){
            case R.id.menu_item_delete_crime:
                NoteLab.get(getActivity()).deleteCrime(note);
                adapter.notifyDataSetChanged();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Note c=((NoteAdapter)getListAdapter()).getItem(position);

        Intent i =new Intent (getActivity(),NotePagerActivity.class);
        i.putExtra(NoteFragment.EXTRA_NOTE_ID,c.getId());
        startActivity(i);
    }



    public class NoteAdapter extends ArrayAdapter<Note>{
        public NoteAdapter(ArrayList<Note> notes){
            super(getActivity(),0, notes);
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            if(convertView == null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }
            Note c= getItem(position);

            TextView titleTextView=(TextView)convertView.findViewById(R.id.crime_list_item_titleTextVeiw);
            titleTextView.setText(c.getTitle());
            TextView dateTextView=(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(NoteFragment.mDateFormatter.format(c.getDate()));
            CheckBox solvedCheckBox=(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.notes_title);
        mNotes = NoteLab.get(getActivity()).getNotes();

        setRetainInstance(true);
        mSubtitleVisible=false;

        NoteAdapter adapter=new NoteAdapter(mNotes);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =super.onCreateView(inflater,container,savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v=getActivity().getLayoutInflater().inflate(R.layout.empty_list,null,false);
        // ******** YOU MUST ADD THE VIEW TO THE VIEW HIERARCHY ******** \\
        getActivity().addContentView(v,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Set the emptyView as the list's EmptyView
        getListView().setEmptyView(v);
    }
}
