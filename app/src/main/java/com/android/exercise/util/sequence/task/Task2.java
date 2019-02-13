package com.android.exercise.util.sequence.task;

import com.android.exercise.ui.activity.queue.TaskSequenceActivity;
import com.android.exercise.util.sequence.QueueTask;

public class Task2 extends QueueTask {
    private TaskSequenceActivity.Callback callback;

    public Task2(TaskSequenceActivity.Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void run() {
        callback.update(getClass().getName());
        controller().proceed();
    }
}
