package me.ma1de.anticheat.util.cpsrecorder;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.ma1de.anticheat.processor.Processor;
import me.ma1de.anticheat.user.User;
import me.ma1de.anticheat.util.CPSRecorder;

import java.util.List;

@Getter
public class CPSRecorderHandler {
    private final List<CPSRecorder> recorders = Lists.newArrayList();

    public CPSRecorder getRecorder(final User user, final Processor processor) {
        return this.recorders
                .stream()
                .filter(recorder -> recorder.getUser().getUuid().equals(user.getUuid()) && recorder.getProcessor().equals(processor))
                .findAny()
                .orElse(null);
    }

    public void addRecorder(final User user, final Processor processor) {
        this.recorders.add(new CPSRecorder(user, processor));
    }

    public void removeRecorder(final User user, final Processor processor) {
        this.recorders.remove(this.getRecorder(user, processor));
    }
}
