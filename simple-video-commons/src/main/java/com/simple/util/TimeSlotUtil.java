package com.simple.util;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TimeSlotUtil
 * @Author Bai
 * @Date 2019/12/19 15:21
 * @Version 1.0
 * @description 判断多个时间段是否出现重叠
 **/
@Slf4j
public class TimeSlotUtil {


    public boolean checkOverlap(List<String> list){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Collections.sort(list);//排序ASC

        boolean flag = false;//是否重叠标识
        for(int i=0; i<list.size(); i++){
            if(i>0){
                //跳过第一个时间段不做判断
                String[] itime = list.get(i).split("-");
                for(int j=0; j<list.size(); j++){
                    //如果当前遍历的i开始时间小于j中某个时间段的结束时间那么则有重叠，反之没有重叠
                    //这里比较时需要排除i本身以及i之后的时间段，因为已经排序了所以只比较自己之前(不包括自己)的时间段
                    if(j==i || j>i){
                        continue;
                    }

                    String[] jtime = list.get(j).split("-");
                    //此处DateUtils.compare为日期比较(返回负数date1小、返回0两数相等、返回正整数date1大)
                    Integer compare = this.compare(sdf.format(new Date())+" "+itime[0]+":00",
                            sdf.format(new Date())+" "+jtime[1]+":00",
                            "yyyy-MM-dd HH:mm:ss");
                    if(compare<0){
                        flag = true;
                        break;//只要存在一个重叠则可退出内循环
                    }
                }
            }

            //当标识已经认为重叠了则可退出外循环
            if(flag){
                break;
            }
        }

        return flag;
    }

    private Integer compare(String DATE1, String DATE2, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return null;
    }
}
