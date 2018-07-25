package kuangyibao.com.kuangyibao.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kuangyibao.com.kuangyibao.R;

/**
 * Created by apple on 18-6-6.
 */

public class CodeDialogView extends Dialog {
    public CodeDialogView(Context context) {
        super(context);
    }

    public CodeDialogView(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;

        public Builder(Context context) {
            this.context = context;
        }


        public CodeDialogView create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CodeDialogView dialog = new CodeDialogView(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_code_view, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
