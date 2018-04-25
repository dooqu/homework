package homework;
import java.util.Comparator;
import java.util.List;


/*
Talk对象代表本题目中的Talk
*/

public class Talk implements Comparable<Talk>
{
    protected String title;
    protected int duration;

    protected Talk(String title, int duration)
    {
        this.title = title;
        this.duration = duration;
    }
    public String getTitle()
    {
        return this.title;
    }
    public int getDuration()
    {
        return this.duration;
    }
    @Override
    public int compareTo(Talk t)
    {
        if(this.duration > t.duration)
            return 1;
        else if(this.duration < t.duration)
            return -1;
        else return 0;
    }

    public static Talk create(String title, int duration) throws Exception
    {
        if(title == null)
            throw new Exception("Talk's title can not be null.");

        if(duration < 5 || duration > 60)
            throw new Exception("Talk's duration argument error.");

        return new Talk(title, duration);
    }
}
