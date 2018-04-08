using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.Events;

public class Core : MonoBehaviour {

	[SerializeField]
	GameObject[] objects;
	public int currentlySelectedItemIndex;

	List<GameObject> instantiatedObjects = new List<GameObject>();

	public GameObject focusedObject;

	[SerializeField]
	public int SelectedPrefabIndex;

	[SerializeField]
	CameraControl controller;

	[SerializeField]
	GameObject positionWhereToSpawnObjects;

	[SerializeField]
	Color colorFocused;

	[SerializeField]
	Color colorDefault;

	[SerializeField]
	Material linerMaterial;

	public void InstantiateObject(int index) {
		GameObject spawnedObject = Instantiate(objects[index], Vector2.zero, Quaternion.identity);

		instantiatedObjects.Add(spawnedObject);
		BoxCollider2D collider = spawnedObject.GetComponent<BoxCollider2D>();
		Vector2 pos = positionWhereToSpawnObjects.transform.position;
		pos.y += collider.bounds.size.y / 2f;
		spawnedObject.transform.position = pos;

		SetObjectAsFocused(spawnedObject);
		Camera.main.transform.position = new Vector3(pos.x, pos.y, -10);
	}

	public void RemoveObject() {

	}

	bool isPausedNow = false;

	public void PauseToggle() {
		isPausedNow = !isPausedNow;

		foreach(GameObject obj in instantiatedObjects) {
			NonPhysicalJoint[] joints = obj.GetComponents<NonPhysicalJoint>();
			foreach(NonPhysicalJoint joint in joints) {
				if(isPausedNow) {
					joint.Pause();
				}
				else {
					joint.Unpause();
				}
			}
		}
	}

	bool IsNowAddingLink = false;
	public void AddLink() {
		if(!focusedObject) {
			return;
		}

		IsNowAddingLink = true;

	}

	public void RotateObject(int direction) {
		if(!focusedObject) {
			return;
		}
		focusedObject.transform.Rotate(0, 0, 45 * -direction);
	}

	int[,] map = new int[50, 50];

	void Start() {

	}

	void SetObjectAsFocused(GameObject toFocus) {
		if (focusedObject)
		{
			focusedObject.GetComponent<SpriteRenderer>().color = colorDefault;
		}
		focusedObject = toFocus;
		focusedObject.GetComponent<SpriteRenderer>().color = colorFocused;
	}

	void RoundVector(ref Vector3 toRound) {
		toRound.x = Mathf.Round(toRound.x * 10f) / 10f;
		toRound.y = Mathf.Round(toRound.y * 10f) / 10f;
		toRound.z = Mathf.Round(toRound.z * 10f) / 10f;
	}
	float startTime = 0f;
	GameObject hitObjectOnStart;
	Vector2 hitAndObjectCenterDelta;
	bool isHitOnFocusedObject = false;

	bool AllowCameraMovement = true;
	void Update() {
		if(Input.touchCount > 0) {
			Touch touch = Input.GetTouch(0);
			Vector2 touchPosition = touch.position;

			RaycastHit2D hit = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(touchPosition), Vector2.zero);

			if(touch.phase == TouchPhase.Began) {
				//If we just started pressing (1 frame when user clicked)
				if (hit && !IsPointerOverUIObject())
				{
					hitObjectOnStart = hit.transform.gameObject;
					hitAndObjectCenterDelta = hit.transform.position - Camera.main.ScreenToWorldPoint(touchPosition);

					if (focusedObject && focusedObject == hit.transform.gameObject)
					{
						isHitOnFocusedObject = true;
					}
					else
					{
						isHitOnFocusedObject = false;
					}
				}
				else {
					hitObjectOnStart = null;
					isHitOnFocusedObject = false;
					hitAndObjectCenterDelta = Vector2.zero;
				}

				startTime = Time.time;
			}
			else if(touch.phase == TouchPhase.Ended) {
				//If we just ended pressing (1 frame when user releasd his finger)
				if (Time.time - startTime < 0.1f && hit && hitObjectOnStart == hit.transform.gameObject)
				{
					if(IsNowAddingLink) {
						hitObjectOnStart = null;
						IsNowAddingLink = false;
						NonPhysicalJoint joint = focusedObject.AddComponent<NonPhysicalJoint>();
						joint.SetConnectedObject(hit.transform.gameObject);

						LineBetweenTwoObjects liner = new GameObject().AddComponent<LineBetweenTwoObjects>();
						liner.materialToSet = linerMaterial;
						liner.object1 = hit.transform.gameObject;
						liner.object2 = focusedObject;

						hitAndObjectCenterDelta = Vector2.zero;
						startTime = 0f;
					}
					else
					{
						hitObjectOnStart = null;
						SetObjectAsFocused(hit.transform.gameObject);
						hitAndObjectCenterDelta = Vector2.zero;
						startTime = 0f;
					}
				}
				isHitOnFocusedObject = false;

				AllowCameraMovement = true;
			}
			else
			{
				//If we are moving our finger
				if (isHitOnFocusedObject)
				{
					Vector3 worldPos = Camera.main.ScreenToWorldPoint(touchPosition);
					worldPos += (Vector3)hitAndObjectCenterDelta;

					RoundVector(ref worldPos);
					worldPos.z = 0;
					focusedObject.transform.position = worldPos;
					AllowCameraMovement = false;
				}
			}
		}

		if (AllowCameraMovement && !IsPointerOverUIObject()) {
			controller.UpdateCamera();
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
