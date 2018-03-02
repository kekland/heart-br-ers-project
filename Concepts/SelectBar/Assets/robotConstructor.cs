using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using UnityEngine.EventSystems;


public class robotConstructor : MonoBehaviour , IPointerClickHandler{

	public List<GameObject> Prefabs;
	public int currentSelectedPrefab;

	public GameObject selectedPrefab;
	GameObject attachedObject;
	public float yOffset = 0f;

	public void OnPointerClick(PointerEventData eventData)
	{


//	attachedObject.AddComponent<FixedJoint>().connectedBody = selectedPrefab.GetComponent<Rigidbody>();

		switch (gameObject.name) {
		case "wheelIm":
			currentSelectedPrefab = 0;
			ChangeSelectedPrefab(false);

			break;
		case "shaftIm":
			currentSelectedPrefab = 1;
			ChangeSelectedPrefab(false);
			break;
		case "PlatformIm":
			currentSelectedPrefab = 2;
			ChangeSelectedPrefab(false);

			break;
		case "ClowIm":
			currentSelectedPrefab = 3;
			ChangeSelectedPrefab(false);
			break;
		case "iftIm":
			currentSelectedPrefab = 2;
			ChangeSelectedPrefab(false);

			break;
		case "HingeIm":
			currentSelectedPrefab = 4;
			ChangeSelectedPrefab(false);

			break;

		}
		selectedPrefab.GetComponent<Rigidbody>().detectCollisions = true;
		selectedPrefab.GetComponent<Rigidbody>().isKinematic = false;
//		selectedPrefab.transform.position = new Vector3(Camera.main.ScreenPointToRay (Input.mousePosition).x, Camera.main.ScreenPointToRay (Input.mousePosition).y);
		//selectedPrefab.GetComponent<Transform> ().position =  Vector3 (Camera.main.ScreenPointToRay (Input.mousePosition));
		Debug.Log(selectedPrefab.transform.position);

	}


	// Use this for initialization
	void Start () {
		Destroy(selectedPrefab);
		//ChangeSelectedPrefab(true);

	}

	// Update is called once per frame
	void Update () {
		RaycastHit hit;
		if(Physics.Raycast(Camera.main.ScreenPointToRay(Input.mousePosition), out hit)) {
			Vector3 rawPosition = hit.point;
			rawPosition.x = (float)Math.Round(rawPosition.x, 1);
			rawPosition.y = (float)Math.Round(rawPosition.y, 1);
			rawPosition.z = (float)Math.Round(rawPosition.z, 1);

			rawPosition.y += selectedPrefab.GetComponent<Collider>().bounds.size.y / 2f + yOffset;

			attachedObject = hit.collider.gameObject;
			selectedPrefab.transform.position = rawPosition;
		}

		if(Input.GetMouseButtonDown(0)) {
			selectedPrefab.GetComponent<Rigidbody>().detectCollisions = true;
			selectedPrefab.GetComponent<Rigidbody>().isKinematic = false;
			Debug.Log(selectedPrefab.transform.position);

			attachedObject.AddComponent<FixedJoint>().connectedBody = selectedPrefab.GetComponent<Rigidbody>();
			ChangeSelectedPrefab(true);
			Destroy(selectedPrefab);
		}

		if(Input.GetKeyDown(KeyCode.UpArrow)) {
			yOffset += 0.1f;
		}
		else if(Input.GetKeyDown(KeyCode.DownArrow)) {
			yOffset -= 0.1f;
		}
	}

	void ChangeSelectedPrefab(bool destroy) {
		selectedPrefab = null;
		selectedPrefab = Instantiate(Prefabs[currentSelectedPrefab]);
		selectedPrefab.GetComponent<Rigidbody>().detectCollisions = false;
		selectedPrefab.GetComponent<Rigidbody>().isKinematic = true;
		yOffset = 0f;
	}
}
