package com.hengda.smart.blelib;

import org.kymjs.kjframe.utils.KJLoger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description 澶勭悊宸ュ叿
 * @author wzq
 * @date 2015-10-8 涓婂崍10:21:44
 * @update (date)
 * @version V1.0
 */
public class IBeaconNoUtil {
	//目标数据
	LinkedList<Integer> lList = new LinkedList<Integer>();
	//	public static final int LINIT_LIST_NO = 6;
	private int bestNoLimit;
	private int totalTempNums;
	// hashMap
	private List<HashMap<String, Integer>> beaconApperNums = new ArrayList<HashMap<String, Integer>>();
	private HashMap<Integer, Integer> noHashMap = new HashMap<Integer, Integer>();
	List<Map.Entry<Integer, Integer>> sortMapList;

	/**
	 * <p>Title: </p>
	 * <p>Description: totalTempNums设置目标容器中存放的最大值的个数，bestNoLimit 选出最优多模次数限制</p>
	 * <p>sample: totalTempNums==5，bestNoLimit 3; [101,102,101,101,106] 101出现的个数满足大于3 则选出最优的的数为101</p>
	 * @author wzq
	 * @date 2015-10-9 上午8:42:50
	 * @update (date)
	 */

	public IBeaconNoUtil(int totalTempNums,int bestNoLimit){
		this.bestNoLimit=bestNoLimit;
		this.totalTempNums=totalTempNums;
	}

	/**
	 * @Description: 设置集合
	 * @param linkList
	 * @return void
	 * @throws
	 * @autour wzq
	 * @date 2015-10-8 上午11:37:01
	 * @update (date)
	 */
	public void setList(LinkedList<Integer> linkList) {
		this.lList = linkList;
	}

	/**
	 *
	 * @Description: 添加 beacon no
	 * @param beaconNo
	 * @return void
	 * @throws
	 * @autour wzq
	 * @date 2015-10-8 上午11:34:39
	 * @update (date)
	 */
	public void addBleNo(int beaconNo) {
		if (lList.size() == totalTempNums) {
			lList.removeFirst();
			lList.addLast(beaconNo);
		} else {
			lList.add(beaconNo);
		}
	}

	/**
	 *
	 * @Description: 得到当前的Linklist
	 * @return
	 * @return LinkedList<Integer>
	 * @throws
	 * @autour wzq
	 * @date 2015-10-8 上午11:35:00
	 * @update (date)
	 */
	public LinkedList<Integer> getLinkList() {
		return lList;
	}

	public int getBestBeaconNo() {
		try {
			if (lList.size() > 1) {
				noHashMap.clear();
				for (int i = 0; i < lList.size(); i++) {
					if (noHashMap.containsKey(lList.get(i))) {
						noHashMap.put(Integer.valueOf(lList.get(i)),
								noHashMap.get(Integer.valueOf(lList.get(i))) + 1);
					} else {
						noHashMap.put(Integer.valueOf(lList.get(i)), 1);
					}
				}
				sortMapList = new ArrayList<Map.Entry<Integer, Integer>>(
						noHashMap.entrySet());

				// 排序前
				for (int i = 0; i < sortMapList.size(); i++) {
					String id = sortMapList.get(i).toString();
//					System.out.println("排序前" + id);
				}

				// 排序
				Collections.sort(sortMapList,
						new Comparator<Map.Entry<Integer, Integer>>() {
							public int compare(Map.Entry<Integer, Integer> o1,
											   Map.Entry<Integer, Integer> o2) {
								return (o2.getValue() - o1.getValue());
							}
						});

				// 排序前
				for (int i = 0; i < sortMapList.size(); i++) {
					String id = sortMapList.get(i).toString();
//					System.out.println("排序后" + id);
				}
				KJLoger.debug(lList.toString());
				if(sortMapList.get(0).getValue()>=bestNoLimit){
					lList.clear();
					return sortMapList.get(0).getKey();
				}else{
					return 0;
				}


			} else {
				return 0;
			}

		} catch (Exception e) {
			return 0;
		}

	}
}
