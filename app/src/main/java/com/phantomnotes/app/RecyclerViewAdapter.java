package com.phantomnotes.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter
        extends ListAdapter<DatabaseObject, RecyclerViewAdapter.BaseViewHolder> {

    private final Context context;
    private final SQLManager databaseHelper;

    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_NO_TITLE = 1;

    public RecyclerViewAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.databaseHelper = new SQLManager(context);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // DiffUtil
    private static final DiffUtil.ItemCallback<DatabaseObject> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DatabaseObject>() {

                @Override
                public boolean areItemsTheSame(
                        @NonNull DatabaseObject oldItem,
                        @NonNull DatabaseObject newItem
                ) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull DatabaseObject oldItem,
                        @NonNull DatabaseObject newItem
                ) {
                    return oldItem.getName().equals(newItem.getName())
                            && oldItem.getNote().equals(newItem.getNote());
                }
            };

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // View types
    @Override
    public int getItemViewType(int position) {
        DatabaseObject item = getItem(position);
        return item.getName() == null || item.getName().isEmpty()
                ? TYPE_NO_TITLE
                : TYPE_DEFAULT;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ViewHolder creation
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_NO_TITLE) {
            View view = inflater.inflate(
                    R.layout.recycler_layout_no_title,
                    parent,
                    false
            );
            return new NoTitleViewHolder(view);
        }

        View view = inflater.inflate(
                R.layout.recycler_layout,
                parent,
                false
        );
        return new DefaultViewHolder(view);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Binding
    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder holder,
            int position
    ) {
        holder.bind(getItem(position));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Base ViewHolder
    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bind(DatabaseObject item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Default ViewHolder
    class DefaultViewHolder extends BaseViewHolder {

        private final TextView noteName;
        private final TextView note;
        private final ConstraintLayout parentLayout;

        DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.name);
            note = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        void bind(DatabaseObject item) {
            noteName.setText(item.getName());
            note.setText(item.getNote());

            parentLayout.setOnClickListener(v -> openNote(item));
            parentLayout.setOnLongClickListener(v -> {
                confirmDelete(item);
                return true;
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // No-title ViewHolder
    class NoTitleViewHolder extends BaseViewHolder {

        private final TextView note;
        private final ConstraintLayout parentLayout;

        NoTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        void bind(DatabaseObject item) {
            note.setText(item.getNote());

            parentLayout.setOnClickListener(v -> openNote(item));
            parentLayout.setOnLongClickListener(v -> {
                confirmDelete(item);
                return true;
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Actions
    private void openNote(DatabaseObject item) {
        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra("noteName", item.getName());
        intent.putExtra("noteDesc", item.getNote());
        intent.putExtra("ID", item.getId());
        context.startActivity(intent);
    }

    private void confirmDelete(DatabaseObject item) {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> deleteItem(item))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(DatabaseObject item) {
        databaseHelper.Delete(item.getId());

        // Create a NEW list (immutable update)
        java.util.List<DatabaseObject> newList =
                new java.util.ArrayList<>(getCurrentList());
        newList.remove(item);

        submitList(newList);
    }
}






/*
package com.example.noteyboi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.phantomnotes.app.R;

import java.util.ArrayList;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder> {

    private final Context context;
    private final ArrayList<DatabaseObject> dbObjects;
    private final SQLManager databaseHelper;

    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_NO_TITLE = 1;

    public RecyclerViewAdapter(Context context, ArrayList<DatabaseObject> objects) {
        this.context = context;
        this.dbObjects = objects;
        this.databaseHelper = new SQLManager(context);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // View Types
    @Override
    public int getItemViewType(int position) {
        String title = dbObjects.get(position).getName();
        return title == null || title.isEmpty()
                ? TYPE_NO_TITLE
                : TYPE_DEFAULT;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ViewHolder creation
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_NO_TITLE) {
            View view = inflater.inflate(
                    R.layout.recycler_layout_no_title,
                    parent,
                    false
            );
            return new NoTitleViewHolder(view);
        }

        View view = inflater.inflate(
                R.layout.recycler_layout,
                parent,
                false
        );
        return new DefaultViewHolder(view);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Binding
    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder holder,
            int position
    ) {
        holder.bind(dbObjects.get(position));
    }

    @Override
    public int getItemCount() {
        return dbObjects.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Base ViewHolder
    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bind(DatabaseObject item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Default ViewHolder (with title)
    class DefaultViewHolder extends BaseViewHolder {

        private final TextView noteName;
        private final TextView note;
        private final ConstraintLayout parentLayout;

        DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.name);
            note = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        void bind(DatabaseObject item) {
            noteName.setText(item.getName());
            note.setText(item.getNote());

            setupClickListeners(item);
        }

        private void setupClickListeners(DatabaseObject item) {
            parentLayout.setOnClickListener(v -> openNote(item));
            parentLayout.setOnLongClickListener(v -> {
                confirmDelete(item);
                return true;
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // No-title ViewHolder
    class NoTitleViewHolder extends BaseViewHolder {

        private final TextView note;
        private final ConstraintLayout parentLayout;

        NoTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        void bind(DatabaseObject item) {
            note.setText(item.getNote());

            parentLayout.setOnClickListener(v -> openNote(item));
            parentLayout.setOnLongClickListener(v -> {
                confirmDelete(item);
                return true;
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Actions
    private void openNote(DatabaseObject item) {
        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra("noteName", item.getName());
        intent.putExtra("noteDesc", item.getNote());
        intent.putExtra("ID", item.getId());
        context.startActivity(intent);
    }

    private void confirmDelete(DatabaseObject item) {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> deleteItem(item))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(DatabaseObject item) {
        int position = dbObjects.indexOf(item);
        if (position == -1) return;

        databaseHelper.Delete(item.getId());
        dbObjects.remove(position);
        notifyItemRemoved(position);
    }
}
*/



/*
package com.example.noteyboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.phantomnotes.app.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder> {
    private final Context mContext;
    SQLManager mDatabaseHelper;
    private final ArrayList<DatabaseObject> dbObjects;



    public RecyclerViewAdapter(Context context, ArrayList<DatabaseObject> Objects) {
        this.mContext = context;
        this.dbObjects = Objects;
    }

    @Override
    public int getItemViewType(int position){
        int type = 0;
        //Default = 0, No title = 1
        String title = dbObjects.get(position).getName();
        if (title.equals("")){
            type = 1;
        }
        return type;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create the view to insert the information into
        if (viewType == 1) {
            //Default = 0, No title = 1
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_no_title, parent, false);
            return new AltViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dbObjects.size();
    }

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder{
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind (int position);
    }

    public class ViewHolder extends BaseViewHolder {
        //Defining the objects in the layout
        TextView noteName;
        TextView note;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.name);
            note = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        public void bind(int position){
            //Populating the layout
            noteName.setText(dbObjects.get(position).getName());
            note.setText(dbObjects.get(position).getNote());
            final int ID = dbObjects.get(position).getId();

            //Prompt to remove note
            parentLayout.setOnLongClickListener(view -> {
                new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            mDatabaseHelper = new SQLManager(mContext);
                            MainActivity.dbObjects.remove(position);
                            MainActivity.adapter.notifyDataSetChanged();
                            mDatabaseHelper.Delete(ID);
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            });

            //Passing the information into the activity that's made when it's clicked then starting it
            parentLayout.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, NotesActivity.class);
                intent.putExtra("noteName", dbObjects.get(position).getName());
                intent.putExtra("noteDesc", dbObjects.get(position).getNote());
                intent.putExtra("ID", dbObjects.get(position).getId());
                mContext.startActivity(intent);
            });
        }
    }

    public class AltViewHolder extends BaseViewHolder {
        //Defining the objects in the layout
        TextView note;
        ConstraintLayout parentLayout;
        public AltViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        public void bind(int position) {
            //Populating the layout
            note.setText(dbObjects.get(position).getNote());
            final int ID = dbObjects.get(position).getId();

            //Prompt to remove note
            parentLayout.setOnLongClickListener(view -> {
                new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            mDatabaseHelper = new SQLManager(mContext);
                            MainActivity.dbObjects.remove(position);
//                        MainActivity.adapter.notifyItemRemoved(position);
                            MainActivity.adapter.notifyDataSetChanged();
                            mDatabaseHelper.Delete(ID);
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            });

            //Passing the information into the activity that's made when it's clicked then starting it
            parentLayout.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, NotesActivity.class);
                intent.putExtra("noteName", dbObjects.get(position).getName());
                intent.putExtra("noteDesc", dbObjects.get(position).getNote());
                intent.putExtra("ID", dbObjects.get(position).getId());
                mContext.startActivity(intent);
            });
        }
    }
}
*/