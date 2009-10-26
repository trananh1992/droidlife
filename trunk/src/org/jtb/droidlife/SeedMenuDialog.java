package org.jtb.droidlife;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SeedMenuDialog extends AlertDialog {
	public static class Builder extends AlertDialog.Builder {
		private Context mContext;
		private GameView mGameView;
		private ArrayList<Seeder> mSeeders;
		private String[] mSeederNames;

		public Builder(Context context, GameView gameView) {
			super(context);
			this.mContext = context;
			this.mGameView = gameView;

			mSeeders = new ArrayList<Seeder>();

			for (int i = 0; i < FileSeedSource.values.size(); i++) {
				ArrayList<Seeder> sds = FileSeedSource.values.get(i)
						.getSeeders();
				mSeeders.addAll(sds);
			}
			Collections.sort(mSeeders);

			mSeederNames = new String[mSeeders.size()];
			for (int i = 0; i < mSeeders.size(); i++) {
				mSeederNames[i] = mSeeders.get(i).getName();
			}

			setItems(mSeederNames, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Seeder seeder;
					seeder = mSeeders.get(which);
					AlertDialog.Builder builder = seeder
							.getSeederDialogBuilder(mContext, mGameView);
					if (builder != null) {
						AlertDialog ad = builder.create();
						ad.show();
					} else {
						mGameView.seed(seeder);
					}
				}
			});

			setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
	}

	public SeedMenuDialog(Context context) {
		super(context);
	}
}
