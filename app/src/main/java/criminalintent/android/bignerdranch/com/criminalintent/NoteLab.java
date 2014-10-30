package criminalintent.android.bignerdranch.com.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ScarS on 28.09.2014.
 */
public class NoteLab {
    public static NoteLab sNoteLab;
    private Context mAppContext;
    private ArrayList<Note> mNotes;

    public static NoteLab get(Context c) {
        if(sNoteLab == null) {
            sNoteLab = new NoteLab(c.getApplicationContext());
        }
        return sNoteLab;
    }

    private NoteLab(Context appContext) {
        mAppContext=appContext;
        mNotes =new ArrayList<Note>();
     }

    public ArrayList<Note> getNotes(){
        return mNotes;
    }

    public void addCrime(Note note) {
        mNotes.add(note);
    }

    public void deleteCrime(Note note) {
        mNotes.remove(note);
    }

    public Note getNote(UUID id){
        for(Note c: mNotes){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }


}
