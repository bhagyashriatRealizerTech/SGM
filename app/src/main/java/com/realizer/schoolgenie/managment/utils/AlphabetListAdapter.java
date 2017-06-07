package com.realizer.schoolgenie.managment.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.model.AddedContactModel;
import com.realizer.schoolgenie.managment.view.CheckableRelativeLayout;
import com.realizer.schoolgenie.managment.view.SelectStudentCheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlphabetListAdapter extends BaseAdapter {

    private ArrayList<AddedContactModel> checkedEmployee;
    Boolean[] chkstate;
    String Flag="";

    public static abstract class Row {}
    
    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }
    
    public static final class Item extends Row {
        public final AddedContactModel text;

        public Item(AddedContactModel text) {
            this.text = text;
        }
    }
    
    private List<Row> rows;
    
    public void setRows(List<Row> rows) {
        this.rows = rows;
        chkstate = new Boolean[rows.size()];

        for (int i = 0; i < rows.size(); i++) {
            chkstate[i] = Boolean.FALSE;
        }
    }

    public void setFlag(String flag)
    {
        this.Flag=flag;
    }

    public void setCheckedEmployeeList(ArrayList<AddedContactModel> checkedEmployee) {
        this.checkedEmployee = checkedEmployee;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Row getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        
        if (getItemViewType(position) == 0) { // Item
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (CheckableRelativeLayout) inflater.inflate(R.layout.row_item, parent, false);
            }
            
            final Item item = (Item) getItem(position);
            TextView textviewDP = (TextView) view.findViewById(R.id.txtinitialPupil);
            TextView firstName = (TextView) view.findViewById(R.id.first_name);
            final SelectStudentCheckBox checkBox = (SelectStudentCheckBox) view.findViewById(R.id.select_Recipient_checkbox);
            checkBox.setTag(position);
            RelativeLayout id_relativeLayout=(RelativeLayout) view.findViewById(R.id.id_relativeLayout);
            id_relativeLayout.setTag(position);
            ImageView userImage = (ImageView)view.findViewById(R.id.img_user_image);


            if(item.text.getProfileimage() != null && !item.text.getProfileimage().equals("") && !item.text.getProfileimage().equalsIgnoreCase("null")) {
                String urlString = item.text.getProfileimage();
                if (!urlString.isEmpty()) {
                    userImage.setVisibility(View.VISIBLE);
                    textviewDP.setVisibility(View.GONE);
                    ImageStorage.setThumbnail(userImage, urlString);
                } else {
                    userImage.setVisibility(View.GONE);
                    textviewDP.setVisibility(View.VISIBLE);
                    String name[] = item.text.getUserName().trim().split(" ");
                    char fchar  = name[0].toUpperCase().charAt(0);
                    char lchar  = name[0].toUpperCase().charAt(0);
                    for(int i =0;i<name.length;i++)
                    {
                        if(!name[i].equals("") && i==0)
                            fchar = name[i].toUpperCase().charAt(0);
                        else if(!name.equals("") && i==(name.length-1))
                            lchar = name[i].toUpperCase().charAt(0);

                    }
                    textviewDP.setText(fchar+lchar);
                }
            }

            if (Flag.equalsIgnoreCase("StuendList"))
            {
                checkBox.setVisibility(View.GONE);
            }
            else
            {
                checkBox.setVisibility(View.VISIBLE);

                if (checkedEmployee != null  && checkedEmployee.contains(item.text)) {
                    ((ListView) parent).setItemChecked(position, true);
                }
                else {
                    ((ListView) parent).setItemChecked(position, false);
                }


            }

            if(Flag.equalsIgnoreCase("MyClass"))
                checkBox.setVisibility(View.GONE);
            else
                checkBox.setVisibility(View.VISIBLE);


            firstName.setText(item.text.getUserName().trim());



/*            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = (Integer) buttonView.getTag();
                    final Item item1 = (Item) getItem(pos);
                    if(isChecked)
                    {
                        if (checkedEmployee != null )
                        {
                            checkedEmployee.add(item1.text);
                        }
                        else
                        {
                            checkedEmployee = new ArrayList<AddedContactModel>();
                            checkedEmployee.add(item1.text);
                        }
                    }
                    else
                    {
                        if (checkedEmployee != null  && checkedEmployee.contains(item1.text)) {
                            int posremove = checkedEmployee.indexOf(item1.text);
                            checkedEmployee.remove(posremove);
                        }
                    }
                    Singlton.setSelectedStudentList(checkedEmployee);
                }
            });*/


        }
        else { // Section
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_section, parent, false);
            }
            
            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(section.text);
        }


        
        return view;
    }

}
