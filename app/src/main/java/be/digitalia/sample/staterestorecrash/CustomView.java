package be.digitalia.sample.staterestorecrash;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.util.AttributeSet;
import android.view.View;

/**
 * The state of this view will not be restored properly on API < 13
 * because ParcelableCompat passes a null ClassLoader on these Android versions
 * which results in a failure to find the "SubSavedState" class.
 */
public class CustomView extends View {

	public CustomView(Context context) {
		super(context);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		return new SavedState(super.onSaveInstanceState());
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		final SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
	}


	protected static class SavedState extends AbsSavedState {
		SubSavedState state;

		public SavedState(Parcelable superState) {
			super(superState);
			this.state = new SubSavedState();
		}

		public SavedState(Parcel source, ClassLoader loader) {
			super(source);
			// The following line will crash during process restoration in Android API < 13 because loader is null.
			state = source.readParcelable(loader);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeParcelable(state, flags);
		}

		public static final Parcelable.Creator<SavedState> CREATOR
				= ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in, ClassLoader loader) {
				return new SavedState(in, loader);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		});
	}


	protected static class SubSavedState implements Parcelable {

		public SubSavedState() {
		}

		public SubSavedState(Parcel source, ClassLoader loader) {
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel parcel, int i) {
		}

		public static final Parcelable.Creator<SubSavedState> CREATOR
				= ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SubSavedState>() {
			@Override
			public SubSavedState createFromParcel(Parcel in, ClassLoader loader) {
				return new SubSavedState(in, loader);
			}

			@Override
			public SubSavedState[] newArray(int size) {
				return new SubSavedState[size];
			}
		});
	}
}
