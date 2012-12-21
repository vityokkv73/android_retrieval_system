package net.deerhunter.ars.activities;

import java.util.ArrayList;

import net.deerhunter.arg.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 18.10.12
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class TestActivity extends Activity {
    ListView smsListView;
    
    ArrayAdapter<String> aa;
    ArrayList<String> smses = new ArrayList<String>();
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
	}
}