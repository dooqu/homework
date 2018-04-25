package homework;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Comparator;

/*
一、整体数据关系
1、一个会议，有多个并行的Track；
2、一个Track包括两个Session：上午的Session和下午的Session，两个Session对象的不同在于时间最大容量和最小容量不同，180分钟和240分钟、0和180分钟；
3、一个Session，包含多个Talk对象， 要求是对应的Talk对象集合的时长总和，不能超过Session对象时间容量；
4、Talk对象包括标题(title)和按分钟计时的时长字段(duration);

二、generate的思路：
1、先调用TalkParser将文本文件中的数据，parse成List<Talk>集合，并按照每个Talk对象的Duration大小,对List<Talk>从大到校排序；
2、生成一个List<Session>集合，并在其中默认先添加一个上午的Session；
3、优先将talk集合中最占时最长的talk，丢进最空闲的Sesssion;
4、如果当前的List<Session>集合放不下List<Talk>中所有的Talk对象，进入下一次循环，并在循环最前，按顺序在List<Session>中增加上午或者下午的Session,继续尝试添加Talk集合，直到全部talk对象都被放进某个Session中；
*/
public class ConferenceManager
{
    //打印会议议程
    protected void printSessions(List<Session> sessions)
    {
        for(int i = 0, n = 1; i < sessions.size(); i++)
        {
            Session currSession = sessions.get(i);

            if(currSession.isMorningSession())
            {
                System.out.println(String.format("Track %d", n));
                n++;
            }
            SimpleTime sTime = new SimpleTime(currSession.isMorningSession()? 9 : 13, 0);
            for(Talk t : currSession.getTalks())
            {
                System.out.println(sTime.toString() + " " + t.title + ":" + t.duration);
                sTime.addMinutes(t.duration);
            }

            if(currSession.isMorningSession())
            {
                System.out.println("12:00PM Lunch");
            }
            else
            {
                System.out.println("05:00PM Networking Event");
            }
        }
    }

    //尝试一次会议议程的编排
    protected boolean generateProcess(List<Session> sessions, List<Talk> talks)
    {
        if(ConferenceUtil.getTotalDuration(talks) > ConferenceUtil.getMaxCapacity(sessions))
        {
            return false;
        }

        //重置所有的session对象
        for(Session s : sessions)
        {
            s.reset();
        }            

        for(int i = 0; i < talks.size(); i++)
        {
            Talk t = talks.get(i);
            sessions.sort(Comparator.reverseOrder());
            boolean added = false;
            for(int j = 0; j < sessions.size(); j++)
            {
                Session currSession = sessions.get(j);
                if(currSession.tryAdd(t))
                {
                    added = true;
                    break;
                }
            }

            if(added == false)
            {
                //如果发现当前的Talk对象没地儿安放，那么返回，增加一个新的Session
                return false;
            }
        }

        sessions.sort(Comparator.reverseOrder());
        //最后整理：按照题目的理解，所有的下午会议，必须结束在4:00~5:00的区间，
        //如果某个下午会结束时间没有在这个区间，那么尝试和其他Session较大的Talk进行交换
        //如果题目理解有误，以下代码可注释
        for(Session s1 : sessions)
        {
            boolean canfind = true;
            while(s1.isAfternoonSession() && s1.getCapacityLeft() > 60 && canfind)
            {
                for(int i = sessions.size() - 1; i >= 0; i--)
                {
                    Session otherSession = sessions.get(i);
                    if(s1 == otherSession)
                    {
                        canfind = false;
                    }

                    if(ConferenceUtil.tryToExangeTalkFromOther(s1, otherSession) == true)
                    {
                        break;
                    }
                }
            }
        }
        return true;
    }


    public void generate(String filepath) throws Exception
    {
        TalkParser talkParser = new TalkParser();
        List<Talk> talks = talkParser.parseFromFile(filepath);
        List<Session> sessions = new ArrayList<Session>(); 
        List<Session> sessionsForTrack = new ArrayList<Session>();

        int n = -1;
        do
        {
            ++n;
            int minCap = (n % 2 == 0)? 0 : 180;
            int maxCap = (n % 2 == 0)? 180 : 240;
            Session newSession = new Session(minCap, maxCap);
            sessions.add(n, newSession);            
            sessionsForTrack.add(n, newSession);
            if(generateProcess(sessions, talks) == false)
            {
                continue;
            }

            this.printSessions(sessionsForTrack);
            break;
        }
        while(true);        
    }

    public static void main(String[] args) throws Exception
    {
        if(args.length != 1)
        {
            System.out.println("usage: java ConferenceManager inputfile");
            return;
        }

        ConferenceManager cfm = new ConferenceManager();
        cfm.generate(args[0]);
    }
}
