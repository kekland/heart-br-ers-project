using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class drag2 : MonoBehaviour {
	private Vector3 screenPoint; 
	// Use this for initialization
	void OnMouseDown(){
		screenPoint = Camera.main.WorldToScreenPoint (gameObject.transform.position);
	}

	void OnMouseDrag(){
		Vector3 currentScreenPoint = new Vector3 (Input.mousePosition.x, Input.mousePosition.y, screenPoint.z); 
		Vector3 currentPos = Camera.main.ScreenToWorldPoint (currentScreenPoint); 
		transform.position = currentPos; 
	}
}
