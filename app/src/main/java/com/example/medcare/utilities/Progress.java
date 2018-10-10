package com.example.medcare.utilities;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress {

  private static ProgressDialog mProgressDialog;

  public static void showSimpleProgressDialog(Context context, String title,
                                              String msg, boolean isCancelable) {
    try {
      if (mProgressDialog == null) {
        mProgressDialog = ProgressDialog.show(context, title, msg);
        mProgressDialog.setCancelable(isCancelable);
      }

      if (!mProgressDialog.isShowing()) {
        mProgressDialog.show();
      }

    } catch (IllegalArgumentException ie) {
      ie.printStackTrace();
    } catch (RuntimeException re) {
      re.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void showSimpleProgressDialog(Context context) {
    showSimpleProgressDialog(context, null, "Loading...", false);
  }
  public static void removeSimpleProgressDialog() {
    try {
      if (mProgressDialog != null) {
        if (mProgressDialog.isShowing()) {
          mProgressDialog.dismiss();
          mProgressDialog = null;
        }
      }
    } catch (IllegalArgumentException ie) {
      ie.printStackTrace();

    } catch (RuntimeException re) {
      re.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
