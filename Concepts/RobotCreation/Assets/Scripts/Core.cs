using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.Events;
using UnityEngine.UI;
public class Core : MonoBehaviour {
	[SerializeField]
	List<GameObject> placeableObjects;
	[SerializeField]
	int currentSelectedObject = 0;

	[SerializeField]
	public bool CurrentlyPlacingObject;

	[SerializeField]
	public bool isObjectFocused;
	public GameObject focusedObject;

	[SerializeField]
	CameraController camController;

	bool allowCameraMovements = true;

	[SerializeField]
	Material defaultConstructionMaterial;

	[SerializeField]
	Material selectedConstructionMaterial;

	[SerializeField]
	Material focusedConstructionMaterial;

	[SerializeField]
	Button[] buttonsToDisable;

	List<GameObject> constructionObjects = new List<GameObject>();

	StructureElement currentlySelectedGameObject;
	void Start() {
		UpdateButtonStates();
	}

	[SerializeField]
	Vector3 objectSpawningPosition;

	GameObject instantiatedObject;
	public void ChangeCurrentlySelectedObject(int index) {
		if(index < 0) {
			index = 0;
		}
		else if(index >= placeableObjects.Count) {
			index = placeableObjects.Count - 1;
		}

		currentSelectedObject = index;
		if (instantiatedObject != null)
		{
			Destroy(instantiatedObject);
		}

		InstantiateNewObject();
	}

	public void InstantiateNewObject() {
		instantiatedObject = Instantiate(placeableObjects[currentSelectedObject], objectSpawningPosition, Quaternion.identity);
		SetPivotObject(instantiatedObject);
		constructionObjects.Add(instantiatedObject.transform.GetChild(0).gameObject);
	}

	public void LockCurrentObject() {
		instantiatedObject = null;
		InstantiateNewObject();
	}

	public void SetItemTransform(Vector3 position) {
		instantiatedObject.transform.position = position;
	}

	public void SetItemTransform(GameObject hitObject, Vector3 position)
	{
		if(hitObject.CompareTag("Structure")) {
			instantiatedObject.transform.position = position;
		}
	}

	public void SetItemTransform(Quaternion rotation)
	{
		instantiatedObject.transform.rotation = rotation;
	}

	public void SetItemTransform(Vector3 position, Quaternion rotation)
	{
		SetItemTransform(position);
		instantiatedObject.transform.rotation = rotation;
	}

	public void SetPivotObject(GameObject obj) {
		if (focusedObject != null)
		{
			focusedObject.GetComponentInChildren<MeshRenderer>().material = defaultConstructionMaterial;
		}
		focusedObject = obj;
		obj.GetComponentInChildren<MeshRenderer>().material = focusedConstructionMaterial;
		camController.pivotObject = obj;
		isObjectFocused = true;

		UpdateButtonStates();
	}

	public void TurnFocusedObject(int direction) {
		Vector3 prevPosition = focusedObject.transform.parent.position;
		focusedObject.transform.parent.Rotate(0, 45 * direction, 0);
		GameObject overlappingObject;
		if (IsOverlapping(focusedObject.gameObject, out overlappingObject))
		{
			Debug.Log("Overlap!");
			prevPosition.y = overlappingObject.transform.parent.position.y;
			prevPosition.y += focusedObject.GetComponent<BoxCollider>().bounds.size.y / 2f;
			prevPosition.y += overlappingObject.GetComponent<BoxCollider>().bounds.size.y / 2f;
			//focusedObject.transform.parent.Rotate(0, -45 * direction, 0);
			focusedObject.transform.parent.position = prevPosition;
		}
	}

	public void TurnFocusedObjectVertical() {
		Vector3 position;
		Vector3 prevPosition;
		prevPosition = focusedObject.transform.parent.position;
		position = focusedObject.transform.parent.position;
		Bounds thisCollliderBounds = focusedObject.GetComponentInChildren<BoxCollider>().bounds;
		position.y -= thisCollliderBounds.size.y / 2f;
		focusedObject.transform.Rotate(0, 0, 90);
		thisCollliderBounds = focusedObject.GetComponentInChildren<BoxCollider>().bounds;
		position.y += thisCollliderBounds.size.y / 2f;
		focusedObject.transform.parent.position = position;

		GameObject overlappingObject;
		if (IsOverlapping(focusedObject.gameObject, out overlappingObject))
		{
			Debug.Log("Overlap!");
			focusedObject.transform.parent.Rotate(0, 0, -90);
			prevPosition.y = overlappingObject.transform.parent.position.y;
			focusedObject.transform.parent.position = prevPosition;
		}
	}
	void UpdateButtonStates() {
		foreach (Button btn in buttonsToDisable)
		{
			btn.interactable = isObjectFocused;
		}
	}

	bool CheckTwoBounds(Bounds b1, Bounds b2) {
		Vector3 b1Min = b1.min;
		Vector3 b1Max = b1.max;
		b1Min.y = b2.min.y;
		b1Max.y = b2.max.y;
		b1.SetMinMax(b1Min, b1Max);

		return b1.Intersects(b2);
	}

	bool IsThereColliderUnder(GameObject consrtuctionToCheck) {
		Bounds thisBounds = consrtuctionToCheck.GetComponent<BoxCollider>().bounds;
		for (int i = 0; i < constructionObjects.Count; i++) {
			GameObject iterationGameObject = constructionObjects[i];
			if(iterationGameObject.transform.position == consrtuctionToCheck.transform.position) {
				continue;
			}
			Bounds iterBounds = iterationGameObject.GetComponent<BoxCollider>().bounds;

			if(CheckTwoBounds(iterBounds, thisBounds)) {
				return true;
			}
		}
		return false;
	}


	bool IsOverlapping(GameObject constructionToCheck, out GameObject with) {
		Bounds thisBounds = constructionToCheck.GetComponent<BoxCollider>().bounds;
		for (int i = 0; i < constructionObjects.Count; i++)
		{
			GameObject iterationGameObject = constructionObjects[i];
			if (iterationGameObject == constructionToCheck)
			{
				continue;
			}
			Bounds iterBounds = iterationGameObject.GetComponent<BoxCollider>().bounds;
			if (thisBounds.Intersects(iterBounds))
			{
				with = iterationGameObject;
				return true;
			}
		}
		with = null;
		return false;
	}

	bool isMovingCurrently;
	Vector3 movingDirection;
	Vector3 stayDirection;
	Vector3 startingPosition;

	double timeStart;


	void Update() {

		if(Input.GetKeyUp(KeyCode.LeftArrow)) {
			ChangeCurrentlySelectedObject(currentSelectedObject - 1);
		}
		else if(Input.GetKeyUp(KeyCode.RightArrow)) {
			ChangeCurrentlySelectedObject(currentSelectedObject + 1);
		}


		if (Input.touchCount > 0 && !IsPointerOverUIObject())
		{
			Touch thisTouch = Input.GetTouch(0);
			Vector2 mousePosition = thisTouch.position;
			RaycastHit hit;
			if (Physics.Raycast(Camera.main.ScreenPointToRay(mousePosition), out hit))
			{
				/*SetItemTransform(hit.point);
				if (Input.GetMouseButtonDown(1))
				{
					if (CurrentlyPlacingObject)
					{
						LockCurrentObject();
					}
					else
					{
						SetPivotObject(hit.transform.gameObject);
					}
				}*/

				if (thisTouch.phase == TouchPhase.Began)
				{
					if (hit.transform.CompareTag("Structure"))
					{
						allowCameraMovements = false;
						isMovingCurrently = true;
						currentlySelectedGameObject = hit.transform.gameObject.GetComponent<StructureElement>();
						currentlySelectedGameObject.GetComponent<MeshRenderer>().material = selectedConstructionMaterial;
						SetPivotObject(currentlySelectedGameObject.gameObject);

						currentlySelectedGameObject.gameObject.layer = 2;
						startingPosition = hit.point;
						timeStart = Time.time;
					}
				}
				else if (thisTouch.phase == TouchPhase.Ended)
				{
					if (isMovingCurrently)
					{
						currentlySelectedGameObject.gameObject.layer = 0;
						allowCameraMovements = true;
						isMovingCurrently = false;
						currentlySelectedGameObject.GetComponent<Collider>().enabled = true;
						startingPosition = Vector3.zero;
						currentlySelectedGameObject = null;
					}
				}
				else
				{
					if (isMovingCurrently && Time.time - timeStart > 0.2)
					{
						allowCameraMovements = false;
						isMovingCurrently = true;
						Vector3 position;
						Vector3 prevPosition = currentlySelectedGameObject.transform.parent.position;
						position.x = Mathf.Round(hit.point.x * 50f) / 50f;
						position.y = Mathf.Round(hit.point.y * 50f) / 50f;
						position.z = Mathf.Round(hit.point.z * 50f) / 50f;
						/*
						if(position.y < currentlySelectedGameObject.parentObject.transform.position.y) {
							if(IsThereColliderUnder(currentlySelectedGameObject.gameObject)) {
								position.y = currentlySelectedGameObject.parentObject.transform.position.y;
							}
						}*/
						Bounds thisCollliderBounds = currentlySelectedGameObject.GetComponent<BoxCollider>().bounds;
						position.y += thisCollliderBounds.size.y / 2f;
						currentlySelectedGameObject.transform.parent.position = position;

						GameObject overlappingObject;
						if(IsOverlapping(currentlySelectedGameObject.gameObject, out overlappingObject)) {
							Debug.Log("Overlap!");
							prevPosition.x = position.x;
							prevPosition.z = position.z;
							prevPosition.y = overlappingObject.transform.parent.position.y;
							prevPosition.y += thisCollliderBounds.size.y / 2f;
							prevPosition.y += overlappingObject.GetComponent<BoxCollider>().bounds.size.y / 2f;


							currentlySelectedGameObject.transform.parent.position = prevPosition;
						}
						else
						{
						}
					}
				}
			}
		}
		else {
			allowCameraMovements = true;
			if (isMovingCurrently && currentlySelectedGameObject != null)
			{
				currentlySelectedGameObject.gameObject.layer = 0;
				currentlySelectedGameObject.GetComponent<Collider>().enabled = true;
				startingPosition = Vector3.zero;
			}
			currentlySelectedGameObject = null;
			isMovingCurrently = false;
		}

		if (allowCameraMovements)
		{
			camController.UpdateCamera();
		}
	}

	private bool IsPointerOverUIObject()
	{
		PointerEventData eventDataCurrentPosition = new PointerEventData(EventSystem.current);
		eventDataCurrentPosition.position = new Vector2(Input.mousePosition.x, Input.mousePosition.y);
		List<RaycastResult> results = new List<RaycastResult>();
		EventSystem.current.RaycastAll(eventDataCurrentPosition, results);
		return results.Count > 0;
	}
}
