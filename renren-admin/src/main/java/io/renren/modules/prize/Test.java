package io.renren.modules.prize;

import io.renren.modules.prize.domain.Prize;
import io.renren.modules.prize.server.PrizeMathRandom;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        int i = 0;
        PrizeMathRandom a = new PrizeMathRandom();

        List<Prize> prizes = new ArrayList<Prize>();

        Prize p1 = new Prize();
        p1.setPrize_name("500块");
        p1.setPrize_weight(1);//奖品的权重设置成1
        prizes.add(p1);

        Prize p2 = new Prize();
        p2.setPrize_name("300块");
        p2.setPrize_weight(2);//奖品的权重设置成2
        prizes.add(p2);

        Prize p3 = new Prize();
        p3.setPrize_name("200块");
        p3.setPrize_weight(3);//奖品的权重设置成3
        prizes.add(p3);

        Prize p4 = new Prize();
        p4.setPrize_name("100块");
        p4.setPrize_weight(4);//奖品的权重设置成4
        prizes.add(p4);

        Prize p5 = new Prize();
        p5.setPrize_name("谢谢参与！");
        p5.setPrize_weight(990);//奖品的权重设置成4
        prizes.add(p5);
        int[] result = new int[prizes.size()];
        System.out.println("抽奖开始");
        //for (i = 0; i < 1000; i++)// 打印100个测试概率的准确性
       // {
            int selected = a.getPrizeIndex(prizes);
            System.out.println("第" + i + "次抽中的奖品为：" + prizes.get(selected).getPrize_name());
            result[selected]++;
            System.out.println("--------------------------------");
        //}
//        System.out.println("抽奖结束");
//        System.out.println("每种奖品抽到的数量为：");
//        System.out.println("一等奖：" + result[0]);
//        System.out.println("二等奖：" + result[1]);
//        System.out.println("三等奖：" + result[2]);
//        System.out.println("四等奖：" + result[3]);
//        System.out.println("谢谢参与：" + result[4]);
    }
}
