package com.weclassroom.lightweight;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import androidx.appcompat.app.AppCompatActivity;

import com.weclassroom.commonutils.io.SPUtils;
import com.weclassroom.livecore.model.ClassStatus;
import com.weclassroom.livecore.model.WcrClassJoinInfo;
import com.weclassroom.liveui.helper.JoinRoomHelper;

/**
 * @author tal
 */
public class DemoActivity extends AppCompatActivity {
    public static final String TAG = DemoActivity.class.getSimpleName();

    private String mUserCode = "";
    private EditText mUserCodeEdit;

    private String mClassId = "";
    private EditText mClassIdEdit;

    private String mUserID = "";
    private EditText mUserIdEdit;

    private String mTeacherID = "";
    private EditText mTeacherIdEdit;

    private String mInstitutionID = "";
    private EditText mInstitutionEdit;

    private String mCourseId = "";
    private EditText mCourseIdEdit;

    private String mCourseTitle = "";
    private EditText mCourseTitleEdit;

    private String mTeacherName = "";
    private EditText mTeacherNameEdit;

    private String mStudentName = "";
    private EditText mStudentNameEdit;

    private RadioGroup mClassTypeRadio;
    private RadioGroup mClassStateRadio;

    private RadioGroup classStatusRadio;
    private RadioButton classStatusBefore;
    private RadioButton classStatusOnging;
    private RadioButton classStatusAfter;

    private Button mButtonJoin;

    //2:大班课； 0：一对一
    ClassStatus mClassStatus = ClassStatus.CLASS_PREPARE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        findView();
        setViews();
    }

    public void findView() {
        mClassIdEdit = findViewById(R.id.roomid);
        mUserIdEdit = findViewById(R.id.userid);
        mTeacherIdEdit = findViewById(R.id.teacherid);
        mInstitutionEdit = findViewById(R.id.institutionid);
        mUserCodeEdit = findViewById(R.id.usercode);
        mCourseIdEdit = findViewById(R.id.courseid);
        mCourseTitleEdit = findViewById(R.id.coursetitle);
        mClassTypeRadio = findViewById(R.id.class_type_radio);
        mClassStateRadio = findViewById(R.id.class_state_radio);
        mButtonJoin = findViewById(R.id.btn_join_room);
        mTeacherNameEdit = findViewById(R.id.teacher_name);
        mStudentNameEdit = findViewById(R.id.student_name);

        classStatusRadio = findViewById(R.id.class_status_radio);
        classStatusBefore = findViewById(R.id.class_status_before);
        classStatusOnging = findViewById(R.id.class_status_onging);
        classStatusAfter = findViewById(R.id.class_status_after);

        mUserIdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStudentNameEdit.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String userId = SPUtils.getString(this, "user_id");
        String teacherId = SPUtils.getString(this, "teacher_id");
        String instId = SPUtils.getString(this, "inst_id");
        String courseId = SPUtils.getString(this, "course_id");
        String classId = SPUtils.getString(this, "class_id");
        String classState = SPUtils.getString(this, "class_state");
        int classType = SPUtils.getInt(this, "class_type");
        if (!isEmpty(classId)) {
            mClassIdEdit.setText(classId);
        }

        if (!isEmpty(userId)) {
            mUserIdEdit.setText(userId);
            mStudentNameEdit.setText(userId);
        }

        if (!isEmpty(teacherId)) {
            mTeacherIdEdit.setText(teacherId);
        }

        if (!isEmpty(courseId)) {
            mCourseIdEdit.setText(courseId);
        }

        if (!isEmpty(instId)) {
            mInstitutionEdit.setText(instId);
        }

        RadioButton bigRadio = findViewById(R.id.class_type_three);
        RadioButton liveRadio = findViewById(R.id.class_type_live);
        RadioButton smallRadio = findViewById(R.id.class_type_smallclass);
        RadioButton groupRadio = findViewById(R.id.class_type_groupclass);

        if (classType >= 0) {
            switch (classType) {
                case WcrClassJoinInfo.ClassInfo.TYPE_BIG_CLASS:
                case WcrClassJoinInfo.ClassInfo.TYPE_BIG_CLASS_2:
                    bigRadio.setChecked(true);
                    break;

                case WcrClassJoinInfo.ClassInfo.TYPE_ONE_TO_ONE:
                    liveRadio.setChecked(true);
                    break;

                case WcrClassJoinInfo.ClassInfo.TYPE_SMALL_CLASS:
                    smallRadio.setChecked(true);
                    break;

                case WcrClassJoinInfo.ClassInfo.TYPE_GROUP_CLASS:
                    groupRadio.setChecked(true);
                    break;

                default:
                    break;
            }
        }
        if (!TextUtils.isEmpty(classState)) {
            if (classState.equals(ClassStatus.CLASS_PREPARE.name())) {
                classStatusBefore.setChecked(true);
            } else if (classState.equals(ClassStatus.CLASS_ONGOING.name())) {
                classStatusOnging.setChecked(true);
            } else {
                classStatusAfter.setChecked(true);
            }
        }
    }

    private boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    public void setViews() {
        mClassStateRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //课程状态
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                if (radioButtonId == R.id.class_state_over) {
                    //回放
                    mClassStatus = ClassStatus.CLASS_OVER;
                } else {
                    //直播
                    mClassStatus = ClassStatus.CLASS_ONGOING;
                }
            }
        });

        mButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                mClassId = mClassIdEdit.getText().toString().trim();
                mUserID = mUserIdEdit.getText().toString().trim();
                mInstitutionID = mInstitutionEdit.getText().toString().trim();
                mTeacherID = mTeacherIdEdit.getText().toString().trim();
                mCourseId = mCourseIdEdit.getText().toString().trim();
                mCourseTitle = mCourseTitleEdit.getText().toString().trim();
                mTeacherName = mTeacherNameEdit.getText().toString().trim();
                mStudentName = mUserID;

                int classType = 0;
                int radioButtonId = mClassTypeRadio.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                if (radioButtonId == R.id.class_type_three) {
                    //大班课
                    classType = 2;
                } else if (radioButtonId == R.id.class_type_live) {
                    //一对一
                    classType = 0;
                } else if (radioButtonId == R.id.class_type_smallclass) {
                    //小班课
                    classType = 7;
                } else if (radioButtonId == R.id.class_type_groupclass) {
                    //小组课
                    classType = 9;
                }
                int stateId = classStatusRadio.getCheckedRadioButtonId();
                String classState = ClassStatus.CLASS_PREPARE.name();
                switch (stateId) {
                    case R.id.class_status_before:
                        classState = ClassStatus.CLASS_PREPARE.name();
                        break;

                    case R.id.class_status_onging:
                        classState = ClassStatus.CLASS_ONGOING.name();
                        break;

                    case R.id.class_status_after:
                        classState = ClassStatus.CLASS_OVER.name();
                        break;
                }

                SPUtils.putString(context, "user_id", mUserID);
                SPUtils.putString(context, "teacher_id", mTeacherID);
                SPUtils.putString(context, "inst_id", mInstitutionID);
                SPUtils.putString(context, "class_id", mClassId);
                SPUtils.putString(context, "course_id", mCourseId);
                SPUtils.putInt(context, "class_type", classType);
                SPUtils.putString(context, "class_state", classState);

                WcrClassJoinInfo joinInfo = new WcrClassJoinInfo();
                WcrClassJoinInfo.ClassInfo classInfo = new WcrClassJoinInfo.ClassInfo();
                WcrClassJoinInfo.User user = new WcrClassJoinInfo.User();
                classInfo.setSchedualStartTime("2019-05-15 11:45:00");
                classInfo.setSchedualEndTime("2019-05-15 23:40:00");
                classInfo.setTeacherAvatar("https://i.weclassroom.com/img/teacherimg.png");
                classInfo.setTeacherID(mTeacherID);
                classInfo.setTeacherName(mTeacherName);
                classInfo.setClassState(ClassStatus.valueOf(classState));
                classInfo.setClassID(mClassId);
                // 0=一对一模式;1=普通大班课 口令方式;2=普通大班课 登录的方式;3=小组课;5=互动大班课 密码方式;6=互动大班课 口令方式;7=小班课;1000=IBL课程
                classInfo.setClasstype(classType);
                classInfo.setDuration(0);
                classInfo.setCourseId(mCourseId);
                classInfo.setClassTitle(mCourseTitle);
                classInfo.setInstitutionID(mInstitutionID);
                classInfo.setWatermarkShow(WcrClassJoinInfo.ClassInfo.LOGO_WATERMARK_GONE);
                //支持手写板
                classInfo.setSupportRobotPen(true);
                user.setUserToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiMiIsInN1YiI6NDQ0LCJpc3MiOiJodHRwOlwvXC9kZXYtYXBpLndlY2xhc3Nyb29tLmNvbVwvdXNlclwvbG9naW4iLCJpYXQiOjE0OTQ0ODExMDAsImV4cCI6MTQ5NzA3MzEwMCwibmJmIjoxNDk0NDgxMTAwLCJqdGkiOiJhYWY0N2ZiZWNjZmUwNzVhYWIxZDRkNzhiNGEwNzI4ZSJ9.WmRoSgIYnKmbDygd_epWVvGqLQgCRQQ5fdD9I3-wABY");
                user.setUserId(mUserID);
                user.setAvatar("https://cloudclass-dev.oss-cn-beijing.aliyuncs.com/avatar/8a3fb1febe02c6b3776b634ce6609f43.jpg");
                user.setUserRole("student");
                user.setUserName(mStudentName);
                if (classType == 2) {
                    //大班课
                    user.setPhoneNumber("18210596230");
                    joinInfo.setUser(user);
                    joinInfo.setClassInfo(classInfo);
                } else if (classType == 0) {
                    //一对一
                    classInfo.setWaitingDocument("https://i.weclassroom.com/static/zby/loading_4_3.html");
                    joinInfo.setUser(user);
                    joinInfo.setClassInfo(classInfo);
                } else if (classType == 7) {
                    //小班课
                    classInfo.setWaitingDocument("https://i.weclassroom.com/static/zby/loading_4_3.html");
                    joinInfo.setUser(user);
                    joinInfo.setClassInfo(classInfo);
                } else if (classType == 9) {
                    //小组课
                    classInfo.setWaitingDocument("https://i.weclassroom.com/static/zby/loading_4_3.html");
                    joinInfo.setUser(user);
                    joinInfo.setClassInfo(classInfo);
                }
                JoinRoomHelper.joinRoom(DemoActivity.this, joinInfo);
            }
        });
    }

}
