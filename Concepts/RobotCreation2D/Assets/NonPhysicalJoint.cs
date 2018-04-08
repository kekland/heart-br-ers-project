using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NonPhysicalJoint : MonoBehaviour {

	GameObject connectedObject;
	Vector2 delta;
	public void SetConnectedObject(GameObject connectTo) {
		connectedObject = connectTo;
		delta = connectTo.transform.position - transform.position;
	}

	bool isPaused = false;

	public void Pause() {
		isPaused = true;
	}

	public void Unpause() {
		isPaused = false;
		SetConnectedObject(connectedObject);
	}

	void Update() {
		if(isPaused) {
			return;
		}

		connectedObject.transform.position = transform.position + (Vector3)delta;
	}
}
