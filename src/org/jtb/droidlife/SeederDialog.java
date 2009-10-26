package org.jtb.droidlife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public abstract class SeederDialog extends AlertDialog {	
	public abstract static class Builder extends AlertDialog.Builder {
		private GameView mGameView;
		protected Seeder mSeeder;
		protected View mLayout;
		
		public Builder(Context context, GameView gameView, Seeder seeder) {
			super(context);
			this.mGameView = gameView;
			this.mSeeder = seeder;

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLayout = inflater.inflate(getLayout(), null);
			setView(mLayout);
			
			initViews();	
			setViews();
			
			setTitle(mSeeder.toString());
			setIcon(android.R.drawable.ic_dialog_info);
			
			setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setSeeder();
							mGameView.seed(mSeeder);
						}
					});			
			setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});			
		}		
		
		
		public abstract int getLayout();	
		public abstract void initViews();
		public abstract void setViews();
		public abstract void setSeeder();
	}
	
	public SeederDialog(Context context) {
		super(context); 
	}	
}
