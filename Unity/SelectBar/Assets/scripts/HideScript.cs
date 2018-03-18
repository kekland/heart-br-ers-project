using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HideScript : MonoBehaviour {
	public Animator anim; 
	public KeyCode attack1; 
	public KeyCode attack2; 

	void Update(){
		if (Input.GetKeyDown(attack1)){
			anim.SetInteger ("HideShowInt", 1);
	}
		if (Input.GetKeyDown(attack2)){
			anim.SetInteger ("HideShowInt", 2);
		}
}
	public void animationEnded(){
		anim.SetInteger ("HideShowInt", 0);
	}
}
