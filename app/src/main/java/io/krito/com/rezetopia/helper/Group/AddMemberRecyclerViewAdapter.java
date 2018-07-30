package io.krito.com.rezetopia.helper.Group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.models.pojo.Group.Member;

public class AddMemberRecyclerViewAdapter extends RecyclerView.Adapter<AddMemberRecyclerViewAdapter.MemberViewHolder> {
    List<Member> members;
    Context context;
    OnItemCanceledListener onItemCanceledListener;

    public interface OnItemCanceledListener{
        void onItemCanceled (Member member);
    }

    public void setList (List<Member>members){
        this.members = members;
    }

    public AddMemberRecyclerViewAdapter (List<Member> member, Context contex,OnItemCanceledListener onItemCanceledListener){
        this.members = member;
        this.context = context;
        this.onItemCanceledListener = onItemCanceledListener;
    }

    @NonNull
    @Override
    public  MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_admin_rv_item,parent,false);
        MemberViewHolder holder = new MemberViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bind(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.member_name_tv)
        TextView name;
        @BindView(R.id.cancel_icon)
        ImageView icon;

        public MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Member member){
            name.setText(member.getName());
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                    onItemCanceledListener.onItemCanceled(member);
                }
            });
        }
        public void removeAt(int position) {
            members.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, members.size());
        }
    }
}
