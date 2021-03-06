package wangdaye.com.geometricweather.utils;

import android.content.Context;
import android.text.TextPaint;

import wangdaye.com.geometricweather.data.entity.model.Location;
import wangdaye.com.geometricweather.data.entity.model.weather.Weather;
import wangdaye.com.geometricweather.utils.manager.ThreadManager;
import wangdaye.com.geometricweather.utils.remoteView.NormalNotificationUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetClockDayCenterUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetClockDayUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetClockDayWeekUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetDayPixelUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetDayUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetDayWeekUtils;
import wangdaye.com.geometricweather.utils.remoteView.WidgetWeekUtils;
import wangdaye.com.geometricweather.utils.widget.PriorityRunnable;

/**
 * Widget utils.
 * */

public class WidgetUtils {

    /** <br> UI. */

    public static void refreshWidgetInNewThread(final Context context,
                                                    final Location location) {
        ThreadManager.getInstance()
                .execute(new PriorityRunnable(false) {
                    @Override
                    public void run() {
                        if (WidgetDayUtils.isEnable(context)) {
                            WidgetDayUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (WidgetDayPixelUtils.isEnable(context)) {
                            WidgetDayPixelUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (WidgetWeekUtils.isEnable(context)) {
                            WidgetWeekUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (WidgetDayWeekUtils.isEnable(context)) {
                            WidgetDayWeekUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (WidgetClockDayUtils.isEnable(context)) {
                            WidgetClockDayUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (WidgetClockDayCenterUtils.isEnable(context)) {
                            WidgetClockDayCenterUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (WidgetClockDayWeekUtils.isEnable(context)) {
                            WidgetClockDayWeekUtils.refreshWidgetView(context, location, location.weather);
                        }
                        if (NormalNotificationUtils.isEnable(context)) {
                            NormalNotificationUtils.buildNotificationAndSendIt(context, location.weather);
                        }
                    }
                });
    }

    public static String[] buildWidgetDayStyleText(Weather weather) {
        String[] texts = new String[] {
                weather.realTime.weather,
                weather.realTime.temp + "℃",
                weather.dailyList.get(0).temps[0] + "°",
                weather.dailyList.get(0).temps[1] + "°"};

        TextPaint paint = new TextPaint();

        float[] widths = new float[4];
        for (int i = 0; i < widths.length; i ++) {
            widths[i] = paint.measureText(texts[i]);
        }

        float maxiWidth = widths[0];
        for (float w : widths) {
            if (w > maxiWidth) {
                maxiWidth = w;
            }
        }

        while (true) {
            boolean[] flags = new boolean[] {false, false, false, false};

            for (int i = 0; i < 2; i ++) {
                if (widths[i] < maxiWidth) {
                    texts[i] = texts[i] + " ";
                    widths[i] = paint.measureText(texts[i]);
                } else {
                    flags[i] = true;
                }
            }
            for (int i = 2; i < 4; i ++) {
                if (widths[i] < maxiWidth) {
                    texts[i] = " " + texts[i];
                    widths[i] = paint.measureText(texts[i]);
                } else {
                    flags[i] = true;
                }
            }

            int n = 0;
            for (boolean flag : flags) {
                if (flag) {
                    n++;
                }
            }
            if (n == 4) {
                break;
            }
        }

        return new String[] {
                texts[0] + "\n" + texts[1],
                texts[2] + "\n" + texts[3]};
    }
}
