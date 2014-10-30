package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ScarS on 06.10.2014.
 */
public class NotePagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Note> mNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mNotes = NoteLab.get(this).getNotes();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int pos) {
                Note note = mNotes.get(pos);
                return NoteFragment.newInstance(note.getId());
            }

            @Override
            public int getCount() {
                return mNotes.size();
            }
        });

        UUID noteId = (UUID) getIntent().getSerializableExtra(NoteFragment.EXTRA_NOTE_ID);
        for (int i = 0; i < mNotes.size(); i++) {
                if (mNotes.get(i).getId().equals(noteId)) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) { }
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }
            public void onPageSelected(int pos) {
                Note note = mNotes.get(pos);
                if (note.getTitle() != null) {
                    setTitle(note.getTitle());
                }
            }
        });
    }
}
