package com.cbjess.arms.utils.logger;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
