package com.woody.productwarehousing.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class DialogManager extends AlertDialog.Builder{

    public AlertDialog alertDialog;

    public DialogManager(@NonNull Context context) {
        super(context);
    }

    public void setBaseOption(CharSequence title, int icon, CharSequence message) {
        resetView();
        this.setTitle(title);
        if (icon != 0) {
            this.setIcon(icon);
        }
        this.setMessage(message);
    }

    //重置Dialog設定，避免呼叫新的Dialog時，和舊的View重疊
    public void resetView() {
        this.setView(null);
        this.setNegativeButton(null,null);
        this.setNeutralButton(null,null);
        this.setPositiveButton(null,null);
    }

    //重置Dialog設定，避免呼叫新的Dialog時，和舊的View重疊
    public void resetAllView() {
        this.setView(null);
        this.setTitle(null);
        this.setIcon(0);
        this.setMessage(null);
        this.setNegativeButton(null,null);
        this.setNeutralButton(null,null);
        this.setPositiveButton(null,null);
    }

    public void setButtonCommand(String negative, String neutral, String positive,
                                 NegativeCommand negativeCommand,
                                 NeutralCommand neutralCommand,
                                 PositiveCommand positiveCommand) {
        if (negative != null) {
            if (negativeCommand != null) {
                this.setNegativeButton(negative, (dialog, which) -> negativeCommand.negativeExecute());
            }
            else {
                this.setNegativeButton(negative, (dialog, which) -> {});
            }
        }

        if (neutral != null) {
            if (neutralCommand != null) {
                this.setNeutralButton(neutral, (dialog, which) -> neutralCommand.neutralExecute());
            }
            else {
                this.setNeutralButton(neutral, (dialog, which) -> {});
            }
        }
        if (positive != null) {
            if (positiveCommand != null)  {
                this.setPositiveButton(positive, (dialog, which) -> positiveCommand.positiveExecute());
            }
            else {
                this.setPositiveButton(positive, (dialog, which) -> {});
            }
        }
    }

    public interface NegativeCommand {
        void negativeExecute();
    }

    public interface NeutralCommand {
        void neutralExecute();
    }

    public interface PositiveCommand {
        void positiveExecute();
    }

    public AlertDialog createDialog() {
        dismissDialog();
        alertDialog = this.create();
        return alertDialog;
    }

    public void dismissDialog() {
        try {
            if (alertDialog != null) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            alertDialog = null;
        }
    }
}
