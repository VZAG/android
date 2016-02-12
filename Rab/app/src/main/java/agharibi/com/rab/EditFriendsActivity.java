package agharibi.com.rab;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;

import java.util.ArrayList;
import java.util.List;

import weborb.client.ant.wdm.Relations;

public class EditFriendsActivity extends ListActivity {

    protected List<BackendlessUser> mBackendlessUsers;

    protected List<String> mFriendsRelation;
    protected BackendlessUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_edit_friends);

        // Allows to select and deselect items in the list.
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the current user.
        mCurrentUser = RabApplication.getUser();

        mFriendsRelation = new ArrayList<>();
        mFriendsRelation.add(ParceConstants.KEY_FRIENDS_RELATION);
        mCurrentUser.setProperty(ParceConstants.KEY_FRIENDS_RELATION, mFriendsRelation);
        Backendless.UserService.update(mCurrentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });


        setProgressBarIndeterminateVisibility(true);
        Backendless.Data.of(BackendlessUser.class).find(new AsyncCallback<BackendlessCollection<BackendlessUser>>() {

            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> users) {

                setProgressBarIndeterminateVisibility(false);
                //sort this list by user name and limit the query for 1000 users.
                mBackendlessUsers = users.getData();

                int i = 0;
                String [] userNames = new String[mBackendlessUsers.size()];

                for(BackendlessUser user : mBackendlessUsers) {
                    userNames[i] = user.getProperty("name").toString();
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        EditFriendsActivity.this,
                        android.R.layout.simple_list_item_checked,
                        userNames);
                setListAdapter(adapter);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                builder.setMessage(R.string.edit_friends_no_data)
                        .setTitle(R.string.error_title)
                        .setPositiveButton(android.R.string.ok, null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Backendless.Data.of(BackendlessUser.class).loadRelations(mBackendlessUsers.get(position), mFriendsRelation, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });




    }
}