package org.jtb.droidlife;

import android.content.Context;
import android.widget.EditText;

public class RandomSeederDialog extends SeederDialog {	
	public static class Builder extends SeederDialog.Builder {
		private EditText mLoadEdit;
		
		public Builder(Context context, GameView gameView, Seeder seeder) {
			super(context, gameView, seeder);
		}		
		
		
		public int getLayout() {
			return R.layout.random_seeder_dialog;
		}
		
		public void initViews() {
			mLoadEdit = (EditText) mLayout.findViewById(R.id.load_edit);
		}
		
		public void setViews() {
			mLoadEdit.setText(Integer.toString(((RandomSeeder)mSeeder).getLoad()));
		}
		
		public void setSeeder() {
			((RandomSeeder)mSeeder).setLoad(Integer.parseInt(mLoadEdit.getText().toString()));
		}
	}
	
	public RandomSeederDialog(Context context) {
		super(context); 
	}	
}
