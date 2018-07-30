package io.krito.com.rezetopia.helper.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.models.pojo.Group.Member;

public class AddAdminsListViewAdapter extends BaseAdapter {

    Context context;
    List<Member> displayedMembers;
    LayoutInflater layoutInflater;
    OnAdminChoosedListener onAdminChoosedListener;


    public AddAdminsListViewAdapter(){}

    public AddAdminsListViewAdapter(Context context, List<Member> members , OnAdminChoosedListener onAdminChoosedListener){
     this.displayedMembers =members;
     this.context =context;
     this.layoutInflater = LayoutInflater.from(context);
     this.onAdminChoosedListener = onAdminChoosedListener;

    }

    public interface OnAdminChoosedListener{
        void OnAdminChoosed(Member member);
    }

    @Override
    public int getCount() {
        return displayedMembers.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        RelativeLayout relativeLayout ;
        TextView adminName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if (convertView == null){
            holder =new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.add_admin_lv_row,null);
            holder.relativeLayout=convertView.findViewById(R.id.add_admin_lv_relativelayout);
            holder.adminName =convertView.findViewById(R.id.admin_name_lv_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.adminName.setText(displayedMembers.get(position).getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, displayedUsers.get(position).getName(),Toast.LENGTH_LONG).show();
                onAdminChoosedListener.OnAdminChoosed(displayedMembers.get(position));
            }
        });

        return convertView;
    }

}
