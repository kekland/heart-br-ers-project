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
	private GameObject attachedObject;
	public float yOffset = 0f;

	public void OnPointerClick(PointerEventData eventData)
	{


		switch (gameObject.name) {
		case "BaseIm":
			currentSelectedPrefab = 0;
			ChangeSelectedPrefab(false);

			break;
		case "MotorIm":
			currentSelectedPrefab = 1;
			ChangeSelectedPrefab(false);
			break; 
		case "ServoIm":
			currentSelectedPrefab = 2;
			ChangeSelectedPrefab(false);

			break;
		case "ShaftIm":
			currentSelectedPrefab = 3;
			ChangeSelectedPrefab(false);
			break; 
		case "HingeIm":
			currentSelectedPrefab = 4;
			ChangeSelectedPrefab(false);
                break;
        case "AngelIm":
            currentSelectedPrefab = 5;
            ChangeSelectedPrefab(false);
            break;
         case "SonorIm":
              currentSelectedPrefab = 6;
              ChangeSelectedPrefab(false);
              break;
        }
		//selectedPrefab.GetComponent<Rigidbody>().detectCollisions = true;
		//selectedPrefab.GetComponent<Rigidbody>().isKinematic = false;
        Debug.Log(selectedPrefab.transform.position);

	}


	// Use this for initialization
	void Start () {
		

    }

    // Update is called once per frame
    void Update () {
		RaycastHit hit;
		if(Physics.Raycast(Camera.main.ScreenPointToRay(Input.mousePosition), out hit)) {
			Vector3 rawPosition = hit.point;
			rawPosition.x = (float)Math.Round(rawPosition.x, 1);
			rawPosition.y = (float)Math.Round(rawPosition.y, 1);
			rawPosition.z = (float)Math.Round(rawPosition.z, 1);

			rawPosition.y += ((selectedPrefab.GetComponent<Collider>().bounds.size.y / 2f) + yOffset);

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
		if(destroy) {
            
		}
		selectedPrefab = null;
		selectedPrefab = Instantiate(Prefabs[currentSelectedPrefab]);
		selectedPrefab.GetComponent<Rigidbody>().detectCollisions = false;
		selectedPrefab.GetComponent<Rigidbody>().isKinematic = true;
		yOffset = 0f;
	}
}
