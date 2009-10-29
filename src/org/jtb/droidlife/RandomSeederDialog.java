package org.jtb.droidlife;

import android.content.Context;
import android.widget.EditText;

public class RandomSeederDialog extends SeederDialog {	
	public static class Builder extends SeederDialog.Builder {
		private EditText mLoadEdit;
		
		public Builder(Context context, int position, Class activityClass) {
			super(context, position, activityClass);
		}		
		
		
		public int getLayout() {
			return R.layout.random_seeder_dialog;
		}
		
		public void initViews() {
			mLoadEdit = (EditText) mLayout.findViewById(R.id.load_edit);
		}
		
		public void setViews() {
			Seeder seeder = SeederManager.getInstance(mContext).getSeeders().get(mPosition);
			mLoadEdit.setText(Integer.toString(((RandomSeeder)seeder).getLoad()));
		}
		
		public void setSeeder() {
			Seeder seeder = SeederManager.getInstance(mContext).getSeeders().get(mPosition);
			((RandomSeeder)seeder).setLoad(Integer.parseInt(mLoadEdit.getText().toString()));
		}
	}
	
	public RandomSeederDialog(Context context) {
		super(context); 
	}	
}
