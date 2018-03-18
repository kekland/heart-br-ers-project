using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class line : MonoBehaviour {
	//private LineRenderer linerenderer; 

	public Transform arduino; 
	public Transform led; 
	public Vector3  v2;
	// Use this for initialization
	void Start () { 
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetMouseButtonDown (0)) {
			CreatingLine ();
		}
		//yield return new WaitForSeconds(1);
	}

	void CreatingLine() {
		var go = new GameObject();
		var lr = go.AddComponent<LineRenderer>();

		var v1 = Input.mousePosition;


		v1.z = 0f;
		v1 = Camera.main.ScreenToWorldPoint(v1);



		lr.SetPosition(0, v1);

		while (Input.GetMouseButtonDown(0)){
			if (v2 != Input.mousePosition) {
				v2 = Input.mousePosition;
				v2.z = 0f;
				v2 = Camera.main.ScreenToWorldPoint (v2);
				lr.SetPosition (1, v2);
			}
			//yield return new WaitForSeconds(1);
		}

	
		//var gun = GameObject.Find("Arduino");
		//var projectile = GameObject.Find("Led");

		
	}
}

