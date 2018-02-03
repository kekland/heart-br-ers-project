using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class createnew : MonoBehaviour {
	public Vector3 pos;
	void Update(){
		if (Input.GetMouseButtonDown (0)) {
			//GameObject cube = GameObject.CreatePrimitive (PrimitiveType.Cube);
			//cube.transform.position = new Vector3(Camera.main.ScreenToWorldPoint(Input.mousePosition).x, Camera.main.ScreenToWorldPoint(Input.mousePosition).y, 0); 
			//Debug.Log(Input.mousePosition);
			//Debug.Log(Camera.main.Sc ToWorldPoint(Input.mousePosition));
		}
	}
	/*void createCube(){
		GameObject cube = GameObject.CreatePrimitive (PrimitiveType.Cube);
		cube.transform.position = Input.mousePosition; 
	}
	void createSphere(){
		GameObject sphere = GameObject.CreatePrimitive (PrimitiveType.Sphere);
		sphere.transform.position = pos; 
	}*/
}
