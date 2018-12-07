package com.gcml.task.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.task.R;
import com.gcml.task.bean.ChannelModel;
import com.gcml.task.ui.inter.OnChannelListener;

import java.util.List;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/10 .
 */
public class ChannelDialogAdapter extends BaseMultiItemQuickAdapter<ChannelModel, BaseViewHolder> {
    private boolean mIsEdit;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;

    public ChannelDialogAdapter(List<ChannelModel> data, ItemTouchHelper helper) {
        super(data);
        mIsEdit = false;
        this.mItemTouchHelper = helper;
        addItemType(ChannelModel.TYPE_MY, R.layout.item_channel_title);
        addItemType(ChannelModel.TYPE_MY_CHANNEL, R.layout.item_channel_more);
        addItemType(ChannelModel.TYPE_OTHER, R.layout.item_channel_title);
        addItemType(ChannelModel.TYPE_OTHER_CHANNEL, R.layout.item_channel_more);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mRecyclerView = (RecyclerView) parent;
        return super.onCreateViewHolder(parent, viewType);
    }

    private OnChannelListener onChannelListener;

    public void OnChannelListener(OnChannelListener onChannelListener) {
        this.onChannelListener = onChannelListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final ChannelModel channel) {
        switch (baseViewHolder.getItemViewType()) {
            case ChannelModel.TYPE_MY:
                baseViewHolder.setText(R.id.tvTitle, channel.getChannelName());
//                baseViewHolder.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!mIsEdit) {
//                            startEditMode(true);
//                        } else {
//                            startEditMode(false);
//                        }
//                    }
//                });
                baseViewHolder.getView(R.id.tv_sort).setTag(true);
                break;
            case ChannelModel.TYPE_MY_CHANNEL:
                baseViewHolder.setText(R.id.tv_channelname, channel.getChannelName())
                        .setVisible(R.id.img_edit, mIsEdit)
                        .addOnClickListener(R.id.img_edit);

                if (channel.getChannelType() != 1) {
                    baseViewHolder.getView(R.id.img_edit).setTag(true);
                    baseViewHolder.getView(R.id.tv_channelname).setTag(false);
                } else {
                    baseViewHolder.getView(R.id.tv_channelname).setTag(true);
                }

                baseViewHolder.getView(R.id.rl_channel).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!mIsEdit) {
                            startEditMode(true);
                        }
//                        mItemTouchHelper.startDrag(baseViewHolder);
                        return false;
                    }
                });

                baseViewHolder.getView(R.id.rl_channel).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!mIsEdit) return false;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                startTime = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                    //当MOVE事件与DOWN事件的触发的间隔时间大于100ms时，则认为是拖拽starDrag
                                    mItemTouchHelper.startDrag(baseViewHolder);
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                startTime = 0;
                                break;
                        }
                        return false;
                    }
                });

                baseViewHolder.getView(R.id.img_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(channel, baseViewHolder);
                    }
                });
                baseViewHolder.getView(R.id.rl_channel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(channel, baseViewHolder);
                    }
                });
                break;
            case ChannelModel.TYPE_OTHER:
                baseViewHolder.setText(R.id.tvTitle, channel.getChannelName())
                        .setText(R.id.tv_sort, "点击下方项目可快速添加");
                baseViewHolder.getView(R.id.tv_sort).setTag(false);
                break;
            case ChannelModel.TYPE_OTHER_CHANNEL:
                baseViewHolder.setText(R.id.tv_channelname, channel.getChannelName())
                        .setVisible(R.id.img_edit, false);
                baseViewHolder.getView(R.id.tv_channelname).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int myLastPosition = getMyLastPosition();
                        int currentPosition = baseViewHolder.getAdapterPosition();
                        //获取到目标View
                        View targetView = mRecyclerView.getLayoutManager().findViewByPosition(myLastPosition);
                        //获取当前需要移动的View
                        View currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);

                        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if (mRecyclerView.indexOfChild(targetView) >= 0 && myLastPosition != -1) {
                            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                            int spanCount = ((GridLayoutManager) manager).getSpanCount();
                            int targetX = targetView.getLeft() + targetView.getWidth();
                            int targetY = targetView.getTop();

                            int myChannelSize = getMyChannelSize();//这里我是为了偷懒 ，算出来我的频道的大小
                            if (myChannelSize % spanCount == 0) {
                                //添加到我的频道后会换行，所以找到倒数第4个的位置
                                View lastFourthView = mRecyclerView.getLayoutManager().findViewByPosition(getMyLastPosition() - 3);
//                                        View lastFourthView = mRecyclerView.getChildAt(getMyLastPosition() - 3);
                                targetX = lastFourthView.getLeft();
                                targetY = lastFourthView.getTop() + lastFourthView.getHeight();
                            }

                            // 推荐频道 移动到 我的频道的最后一个
                            channel.setItemtype(ChannelModel.TYPE_MY_CHANNEL);//改为推荐频道类型
                            channel.setChannelSelect(true);

                            if (onChannelListener != null)
                                onChannelListener.onMoveToMyChannel(currentPosition, myLastPosition + 1);
                            startAnimation(currentView, targetX, targetY);
                        } else {
                            channel.setItemtype(ChannelModel.TYPE_MY_CHANNEL);//改为推荐频道类型
                            channel.setChannelSelect(true);

                            if (myLastPosition == -1) myLastPosition = 0;//我的频道没有了，改成0
                            if (onChannelListener != null)
                                onChannelListener.onMoveToMyChannel(currentPosition, myLastPosition + 1);
                        }
                    }
                });

                break;
        }
    }

    private void itemClick(ChannelModel channel, BaseViewHolder baseViewHolder) {
        //执行删除，移动到推荐频道列表
        if (mIsEdit) {
            if (channel.getChannelType() == 1) return;
            int otherFirstPosition = getOtherFirstPosition();
            int currentPosition = baseViewHolder.getAdapterPosition();
            //获取到目标View
            View targetView = mRecyclerView.getLayoutManager().findViewByPosition(otherFirstPosition);
            //获取当前需要移动的View
            View currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
            // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
            // 如果在屏幕内,则添加一个位移动画
            if (mRecyclerView.indexOfChild(targetView) >= 0 && otherFirstPosition != -1) {
                RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                int spanCount = ((GridLayoutManager) manager).getSpanCount();
                int targetX = targetView.getLeft();
                int targetY = targetView.getTop();
                int myChannelSize = getMyChannelSize();//这里我是为了偷懒 ，算出来我的频道的大小
                if (myChannelSize % spanCount == 1) {
                    //我的频道最后一行 之后一个，移动后
                    targetY -= targetView.getHeight();
                }
                //我的频道 移动到 推荐频道的第一个
                channel.setItemtype(ChannelModel.TYPE_OTHER_CHANNEL);//改为推荐频道类型
                channel.setChannelSelect(false);

                if (onChannelListener != null)
                    onChannelListener.onMoveToOtherChannel(currentPosition, otherFirstPosition - 1);
                startAnimation(currentView, targetX, targetY);
            } else {
                channel.setItemtype(ChannelModel.TYPE_OTHER_CHANNEL);//改为推荐频道类型
                channel.setChannelSelect(false);
                if (otherFirstPosition == -1) otherFirstPosition = mData.size();
                if (onChannelListener != null)
                    onChannelListener.onMoveToOtherChannel(currentPosition, otherFirstPosition - 1);
            }
        } else {
            if (onChannelListener != null) {
                onChannelListener.onFinish(channel.getChannelName());
            }
        }
    }

    public int getMyChannelSize() {
        int size = 0;
        for (int i = 0; i < mData.size(); i++) {
            ChannelModel channel = (ChannelModel) mData.get(i);
            if (channel.getItemType() == ChannelModel.TYPE_MY_CHANNEL) {
                size++;
            }
        }
        return size;

    }

    /**
     * 我的频道最后一个的position
     *
     * @return
     */
    private int getMyLastPosition() {
        for (int i = mData.size() - 1; i > -1; i--) {
            ChannelModel channel = (ChannelModel) mData.get(i);
            if (ChannelModel.TYPE_MY_CHANNEL == channel.getItemType()) {
                //找到第一个直接返回
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取推荐频道列表的第一个position
     *
     * @return
     */
    private int getOtherFirstPosition() {
        //之前找到了第一个pos直接返回
//        if (mOtherFirstPosition != 0) return mOtherFirstPosition;
        for (int i = 0; i < mData.size(); i++) {
            ChannelModel channel = (ChannelModel) mData.get(i);
            if (ChannelModel.TYPE_OTHER_CHANNEL == channel.getItemType()) {
                //找到第一个直接返回
                return i;
            }
        }
        return -1;
    }

    private void startAnimation(final View currentView, int targetX, int targetY) {
        final ViewGroup parent = (ViewGroup) mRecyclerView.getParent();
        final ImageView mirrorView = addMirrorView(parent, currentView);
        TranslateAnimation animator = getTranslateAnimator(targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);//暂时隐藏
        mirrorView.startAnimation(animator);
        animator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parent.removeView(mirrorView);//删除添加的镜像View
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);//显示隐藏的View
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 添加需要移动的 镜像View
     */
    private ImageView addMirrorView(ViewGroup parent, View view) {
        view.destroyDrawingCache();
        //首先开启Cache图片 ，然后调用view.getDrawingCache()就可以获取Cache图片
        view.setDrawingCacheEnabled(true);
        ImageView mirrorView = new ImageView(view.getContext());
        //获取该view的Cache图片
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        //销毁掉cache图片
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);//获取当前View的坐标
        int[] parenLocations = new int[2];
        mRecyclerView.getLocationOnScreen(parenLocations);//获取RecyclerView所在坐标
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);//在RecyclerView的Parent添加我们的镜像View，parent要是FrameLayout这样才可以放到那个坐标点
        return mirrorView;
    }

    private int ANIM_TIME = 360;

    /**
     * 获取位移动画
     */
    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }


    private void startEditMode(boolean isEdit) {
        mIsEdit = isEdit;
        int visibleChildCount = mRecyclerView.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = mRecyclerView.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit);
            TextView tvName = (TextView) view.findViewById(R.id.tv_channelname);
//            TextView tvEdit = (TextView) view.findViewById(R.id.tv_edit);
            TextView tvSort = (TextView) view.findViewById(R.id.tv_sort);

            if (imgEdit != null) {
                imgEdit.setVisibility(imgEdit.getTag() != null && isEdit ? View.VISIBLE : View.INVISIBLE);
            }

            if (tvName != null) {
                if (tvName.getTag() == null) return;
                if (isEdit && (Boolean) tvName.getTag()) {
                    tvName.setTextColor(Color.GRAY);
                } else {
                    tvName.setTextColor(Color.BLACK);
                }
            }

//            if (tvEdit != null) {
//                if (isEdit) {
//                    tvEdit.setText("完成");
//                } else {
//                    tvEdit.setText("编辑");
//                }
//            }

            if (tvSort != null) {
                if (!(Boolean) tvSort.getTag()) return;
                if (isEdit) {
                    tvSort.setText("拖动可以排序");
                } else {
                    tvSort.setText("长按编辑项目");
                }
            }
        }
    }
}
