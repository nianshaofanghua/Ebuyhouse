package com.yidankeji.cheng.ebuyhouse.housemodule.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.CallBack;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.SubmitRoomTypeMode;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class SubmitTypeAapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<SubmitRoomTypeMode> list;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String type;

    public SubmitTypeAapter(Activity activity, ArrayList<SubmitRoomTypeMode> list, String type) {
        this.activity = activity;
        this.list = list;
        this.type = type;
    }

    public void changeFirstTitle(String title) {
        list.get(0).setTitle(title + " Rent");
        switch (title) {
            case "Month":
                list.get(0).setHint("/MO");
                break;
            case "Half year":
                list.get(0).setHint("/YR");
                break;
            case "Year":
                list.get(0).setHint("/HYR");
                break;
            default:
                break;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_submitroom_layout, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.item_submitroom_title);
            holder.editText = (EditText) convertView.findViewById(R.id.item_submitroom_edittext);
            holder.line = (View) convertView.findViewById(R.id.item_submitroom_line);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.item_submitroom_layout);
            holder.must = convertView.findViewById(R.id.must);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final SubmitRoomTypeMode mode = list.get(position);
        final String tag = mode.getTitle();
        if (mode.getHint() != null) {
            holder.editText.setHint(mode.getHint());
        }
        holder.title.setText(mode.getTitle());
        if (position == 0) {
            if (type.equals("rent")) {
                ///  holder.title.setText("Rent/month");
                // holder.editText.setHint("$/mo");
            } else {
                holder.editText.setHint("$");
            }
        }
        if (tag.equals("Kitchen") || tag.equals("Lot-sqft") || tag.equals("MIS")) {
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.line.setVisibility(View.GONE);
        }
        String tag2 = mode.getTag();
        if (tag2.equals("apn")) {
            // EditMoneyUtils.getNumber(holder.editText);
            holder.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        } else if (tag2.equals("mls")) {
            holder.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        }
        if (tag.equals("Bathrooms")) {
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (tag.equals("Living-sqft")) {
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        }
        if (tag.equals("year_build")) {
            holder.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        }
        if (TextUtils.isEmpty(list.get(position).getScannerEdit())) {
            holder.editText.setText("");
        } else {
            holder.editText.setText(list.get(position).getScannerEdit());
        }

        if(tag2.equals("deposit")){
            holder.editText.setCursorVisible(false);
            holder.editText.setFocusable(false);
            holder.editText.setFocusableInTouchMode(false);
        }else {
            holder.editText.setFocusable(true);
            holder.editText.setCursorVisible(true);
            holder.editText.setFocusableInTouchMode(true);
            holder.editText.requestFocus();
        }



        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = holder.editText.getText().toString();


                if (string == null) {
                    string = "";
                }
                String tag1 = mode.getTag();
                if (tag1.equals("apn") || tag1.equals("mls")) {

                    String s1 = string.replace("-", "");

                    if (tag1.equals("apn") && s1.length() > 5) {

                        getData(tag1, s1);
                    }
                    if (tag1.equals("mls") && s1.length() > 5) {

                        getData(tag1, s1);
                    }


                } else if (tag1.equals("price") && list.get(1).getTag().equals("deposit")) {
                    hashMap.put(tag1, string);
                    if(mOnClickListening!=null){
                        mOnClickListening.OnClick(position,string);
                    }
                } else {
                    hashMap.put(tag1, string);
                }


                //                } else if (tag1.equals("price") && list.get(1).getTag().equals("deposit")) {
////                    hashMap.put(tag1, string);
////                    hashMap.put("deposit",string);
////                    list.get(1).setScannerEdit(string);
//
//                }

                String text = holder.editText.getText().toString();
                if (tag.equals("Bathrooms")) {
                    if (text.toString().split("\\.").length == 2) {
                        if (!text.toString().split("\\.")[1].equals("5")) {
                            holder.editText.setText(text.toString().split("\\.")[0] + "");
                        }
                    }
                }
                list.get(position).setScannerEdit(string);
            }
        });
        holder.editText.setText(mode.getScannerEdit());

if(mode.getTitle().equals("Garages")||mode.getTitle().equals("MLS#")){
    holder.must.setVisibility(View.GONE);
}else {
    holder.must.setVisibility(View.VISIBLE);
}








        return convertView;
    }

    class ViewHolder {
        TextView title,must;
        EditText editText;
        View line;
        LinearLayout layout;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    private void getData(final String c, final String v) {
        new SubmitRoomHttpUtils(activity).getCanShuDataHttp(c, v, new CallBack.HttpUtilsListening() {
            @Override
            public void getHttpUtilsListening(int statusCode, String response) {
                Log.i(statusCode + "", response);
                if (statusCode == 1) {
                    try {
                        if (response != null) {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                boolean isExist = data.getBoolean("isExist");
                                if (isExist) {
                                    hashMap.put(c, "");
                                } else {
                                    Log.e("lozz", "apn==" + v);
                                    hashMap.put(c, v);
                                }
                            } else if (state == 703) {
                                new LanRenDialog((Activity) activity).onlyLogin();

                            } else {
                                ToastUtils.showToast(activity, jsonObject.getString("message"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    OnClickListening mOnClickListening;

    public void setOnClickListening(OnClickListening onClickListening) {
        mOnClickListening = onClickListening;
    }

    public interface OnClickListening {
        public void OnClick(int postion, String text);
    }




    public void updataView(int posi, ListView listView,String text) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.editText.setText(text);
            list.get(posi).setScannerEdit(text);
        } else {

        }
    }



}
