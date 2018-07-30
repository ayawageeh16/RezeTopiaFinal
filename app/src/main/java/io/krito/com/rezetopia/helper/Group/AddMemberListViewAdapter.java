package io.krito.com.rezetopia.helper.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.models.pojo.Group.Member;

public class AddMemberListViewAdapter extends BaseAdapter {

    Context context;
    List<Member> displayedUsers;
    LayoutInflater layoutInflater;
    AddMemberListViewAdapter.OnMemberChoosedListener onMemberChoosedListener;


    public interface OnMemberChoosedListener{
        void onMemberChoosedListener(Member member);
    }

    public AddMemberListViewAdapter (Context context, List<Member> displayedUsers, OnMemberChoosedListener onMemberChoosedListener){
        this.context=context;
        this.displayedUsers=displayedUsers;
        this.onMemberChoosedListener =onMemberChoosedListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return displayedUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder ;
//
//        if (convertView == null){
//            holder =new AddAdminsListViewAdapter.ViewHolder();
//            convertView = layoutInflater.inflate(R.layout.add_admin_lv_row,null);
//            holder.relativeLayout=convertView.findViewById(R.id.add_admin_lv_relativelayout);
//            holder.adminName =convertView.findViewById(R.id.admin_name_lv_tv);
//            convertView.setTag(holder);
//        }else {
//            holder = (AddAdminsListViewAdapter.ViewHolder) convertView.getTag();
//        }
//
//
//        holder.adminName.setText(displayedMembers.get(position).getName());
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, displayedUsers.get(position).getName(),Toast.LENGTH_LONG).show();
//                onAdminChoosedListener.OnAdminChoosed(displayedMembers.get(position));
//            }
//        });

        return convertView;
    }
}
