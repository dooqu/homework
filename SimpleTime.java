package homework;
/*
SimpleTime是一个简单的以12制显示时间字段的工具类
类内部使用hour和minute分别对小时和分钟进行计数；
内部计算时，为了更好的区分AM和PM，hour字段本质还是采用24进制，只在在输出显示的时候进行12进制转换：
转换的机制是如hour在13~23点区间，进行%12运算， 显示成1~11点；
AM和PM的范围定位规则为：
AM : 00:00 ~ 11:59
PM : 12:00 ~ 23:00
*/

class SimpleTime
{
    protected int hour;
    protected int minute;

    public SimpleTime(int hour, int minute)
    {
        this.hour = hour;
        this.minute = minute;
        //调用一次addMinutes,本质上是对两个字段进行进制格式化
        this.addMinutes(0);
    }

    //minute字段本质上还是按照24进制进行计数
    //显示的时候，如果是13~23点区间，%12，显示成1~11点
    public void addMinutes(int minutes)
    {
        this.minute += minutes;
        //如果分钟字段超过60，小时字段进位
        if(this.minute >= 60)
        {
            this.hour += (this.minute / 60);
            this.minute %= 60;
        }

        //如果小时字段超过24，从下一天开始计数
        if(this.hour >= 24)
        {
            this.hour %= 24;
        }
    }

    //返回这个当前时间是"AM"还是"PM"
    public String getType()
    {
        //AM是从00:00~11:59
        //PM是从12:00~23:59
        if(this.hour >= 0 && this.hour < 12)
        {
            return "AM";
        }
        return "PM";
    }

    @Override
    public String toString()
    {
        int formatHour = this.hour % 12;
        String hourField = (formatHour < 10)? "0" + String.valueOf(formatHour) : String.valueOf(formatHour);
        String minuteField = (this.minute < 10)? "0" + String.valueOf(minute) : String.valueOf(minute);
        return String.format("%s:%s%s", hourField, minuteField, getType());
    }
}
