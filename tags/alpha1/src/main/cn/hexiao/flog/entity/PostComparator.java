package cn.hexiao.flog.entity;

import java.util.Comparator;

public class PostComparator implements Comparator<Post> {
	static final long serialVersionUID = 198408231108524L;

	public int compare(Post o1, Post o2) {
		if(o1.getId() > o2.getId()) {
			return -1;
		} else if(o1.getId() < o2.getId()) {
			return 1;
		}
		return o1.getTitle().compareTo(o2.getTitle());
	}

 

}
