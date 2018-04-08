using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.Events;
using UnityEngine.UI;
public class CoreNew : MonoBehaviour
{
	[SerializeField]
	public bool CurrentlyPlacingObject;

	[SerializeField]
	CameraController camController;

	[SerializeField]
	public bool AllowCameraMovements = true;

	[SerializeField]
	Material defaultConstructionMaterial;

	[SerializeField]
	Material selectedConstructionMaterial;

	[SerializeField]
	Material focusedConstructionMaterial;

	[SerializeField]
	GameObject constructionObjectPrefab;

	[SerializeField]
	Button[] buttonsToDisable;

	List<StructureElement> constructionObjects = new List<StructureElement>();

	StructureElement focusedObject;

	[SerializeField]
	Vector3 objectSpawningPosition;

	[SerializeField]
	GameObject toPivotWhenNothingLeft;

	List<string> actions = new List<string>();
	int currentActionOffset = 0;
	//trx INDEX (V3Old) (V3New) (QOld) (QNew)
	public void InstantiateNewObject()
	{
		GameObject instantiatedObject = Instantiate(constructionObjectPrefab, objectSpawningPosition, Quaternion.identity);
		StructureElement elementObject = instantiatedObject.transform.GetChild(0).GetComponent<StructureElement>();
		elementObject.positioner = elementObject.transform.parent;
		SetPivotObject(elementObject);
		constructionObjects.Add(elementObject);

		actions.Add(string.Format("add {0} {1} {2}",
		constructionObjects.IndexOf(focusedObject),
		Utils.DeserializeVector3(focusedObject.positioner.position),
		Utils.DeserializeQuaternion(focusedObject.positioner.rotation)));
	}
	/*
	public void Undo() {
		if (currentActionOffset - actions.Count == 0)
		{
			return;
		}
		currentActionOffset--;
		string action = actions[actions.Count - currentActionOffset];
		string[] actionParams = action.Split(' ');

		if(actionParams[0] == "trx") {
			//Transform
			int index = int.Parse(actionParams[1]);
			Vector3 fromPos = Utils.SerializeVector3(actionParams[2]);
			Vector3 toPos = Utils.SerializeVector3(actionParams[3]);
			Quaternion fromRot = Utils.SerializeQuaternion(actionParams[4]);
			Quaternion toRot = Utils.SerializeQuaternion(actionParams[5]);

			constructionObjects[index].positioner.position = fromPos;
			constructionObjects[index].positioner.rotation = fromRot;
		}
		else if(actionParams[0] == "rem") {

		}
	}*/

	public void MoveObject(int direction) {
		RemoveObjectFromMap(focusedObject.gameObject);
		Vector3 basePosition = focusedObject.positioner.position;
		float colliderHeight = focusedObject.GetComponent<BoxCollider>().bounds.size.y;
		int index = 1;
		while(true) {
			Vector3 modifiedPosition = basePosition;
			modifiedPosition.y += direction * colliderHeight * index;
			if(modifiedPosition.y > 0.25f || modifiedPosition.y < -0.25f) {
				focusedObject.positioner.position = basePosition;
				AddObjectToMap(focusedObject.gameObject);
				return;
			}

			focusedObject.positioner.position = modifiedPosition;
			if (CheckBoundsOverlap(focusedObject.gameObject))
			{
				index++;
				continue;
			}
			else {
				AddObjectToMap(focusedObject.gameObject);
				break;
			}
		}
	}


	public void RotateObject(int direction) {
		RemoveObjectFromMap(focusedObject.gameObject);
		Quaternion baseRotation = focusedObject.positioner.rotation;

		focusedObject.positioner.Rotate(0, 90 * direction, 0);
		if (CheckBoundsOverlap(focusedObject.gameObject))
		{
			focusedObject.positioner.rotation = baseRotation;
		}
		else {
			actions.Add(string.Format("trx {0} {1} {2} {3} {4}",
			constructionObjects.IndexOf(focusedObject),
			Utils.DeserializeVector3(focusedObject.positioner.position),
			Utils.DeserializeVector3(focusedObject.positioner.position),
			Utils.DeserializeQuaternion(baseRotation),
			Utils.DeserializeQuaternion(focusedObject.positioner.rotation)));
		}
		AddObjectToMap(focusedObject.gameObject);
	}

	public void AddJoint() {
		if(constructionObjects.Count < 2) {
			return;
		}

	}
  void AddJoint(StructureElement s1, StructureElement s2) {
		s2.positioner.parent = s1.positioner;
	}

	void AddToPosition(ref Transform trans, float x = 0f, float y = 0f, float z = 0f) {
		Vector3 v3 = trans.position;
		v3.x += x; v3.y += y; v3.z += z;
		trans.position = v3;
	}
	public void RotateObjectVertical() {
		RemoveObjectFromMap(focusedObject.gameObject);
		Quaternion baseRotation = focusedObject.positioner.rotation;
		Vector3 basePosition = focusedObject.positioner.position;

		float colliderHeight = focusedObject.GetComponent<BoxCollider>().bounds.size.y;
		AddToPosition(ref focusedObject.positioner, y: -colliderHeight / 2f);
		focusedObject.positioner.Rotate(0, 0, 90);
		colliderHeight = focusedObject.GetComponent<BoxCollider>().bounds.size.y;
		AddToPosition(ref focusedObject.positioner, y: colliderHeight / 2f);

		if (CheckBoundsOverlap(focusedObject.gameObject))
		{
			focusedObject.positioner.rotation = baseRotation;
			focusedObject.positioner.position = basePosition;
		}
		else {
			actions.Add(string.Format("trx {0} {1} {2} {3} {4}",
			constructionObjects.IndexOf(focusedObject),
			Utils.DeserializeVector3(basePosition),
			Utils.DeserializeVector3(focusedObject.positioner.position),
			Utils.DeserializeQuaternion(baseRotation),
			Utils.DeserializeQuaternion(focusedObject.positioner.rotation)));
		}
		AddObjectToMap(focusedObject.gameObject);
	}

	public void RemoveObject() {
		actions.Add(string.Format("rem {0} {1} {2}",
		constructionObjects.IndexOf(focusedObject),
		Utils.DeserializeVector3(focusedObject.positioner.position),
		Utils.DeserializeQuaternion(focusedObject.positioner.rotation)));

		RemoveObjectFromMap(focusedObject.gameObject);
		constructionObjects.Remove(focusedObject);
		Destroy(focusedObject.positioner.gameObject);

		if(constructionObjects.Count == 0) {
			focusedObject = null;
			camController.pivotObject = toPivotWhenNothingLeft;
		}
		else
		{
			SetPivotObject(constructionObjects[constructionObjects.Count - 1]);
		}
	}

	#region MapInteractions
	int[,,] robotMap = new int[50, 50, 50];

	int AbsolutePointToRelativeMapPoint(float position)
	{
		position += 0.25f;
		return Mathf.RoundToInt(position * 100f);
	}

	public void SetPivotObject(StructureElement obj)
	{
		if (focusedObject != null)
		{
			focusedObject.GetComponentInChildren<MeshRenderer>().material = defaultConstructionMaterial;
		}
		focusedObject = obj;
		obj.GetComponentInChildren<MeshRenderer>().material = focusedConstructionMaterial;
		camController.pivotObject = obj.gameObject;
	}

	void FillMapBounds(Bounds bounds, int fillWith = 1)
	{
		for (int x = AbsolutePointToRelativeMapPoint(bounds.min.x); x < AbsolutePointToRelativeMapPoint(bounds.max.x); x++)
		{
			for (int y = AbsolutePointToRelativeMapPoint(bounds.min.y); y < AbsolutePointToRelativeMapPoint(bounds.max.y); y++)
			{
				for (int z = AbsolutePointToRelativeMapPoint(bounds.min.z); z < AbsolutePointToRelativeMapPoint(bounds.max.z); z++)
				{
					if(x < 0 || y < 0 || z < 0 || x >= 50 || y >= 50 || z >= 50) continue;
					robotMap[x, y, z] = fillWith;
				}
			}
		}
	}

	bool CheckBoundsOverlap(Bounds bounds)
	{
		for (int x = AbsolutePointToRelativeMapPoint(bounds.min.x); x < AbsolutePointToRelativeMapPoint(bounds.max.x); x++)
		{
			for (int y = AbsolutePointToRelativeMapPoint(bounds.min.y); y < AbsolutePointToRelativeMapPoint(bounds.max.y); y++)
			{
				for (int z = AbsolutePointToRelativeMapPoint(bounds.min.z); z < AbsolutePointToRelativeMapPoint(bounds.max.z); z++)
				{
					if (x < 0 || y < 0 || z < 0 || x >= 50 || y >= 50 || z >= 50) continue;
					if (robotMap[x, y, z] != 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	bool CheckBoundsOverlap(GameObject obj) {
		return CheckBoundsOverlap(obj.GetComponent<BoxCollider>().bounds);
	}

	void RecalculateMap()
	{
		for (int x = 0; x < 50; x++)
		{
			for (int y = 0; y < 50; y++)
			{
				for (int z = 0; z < 50; z++)
				{
					robotMap[x, y, z] = 0;
				}
			}
		}
		foreach (StructureElement constructionObject in constructionObjects)
		{
			BoxCollider collider = constructionObject.GetComponent<BoxCollider>();
			FillMapBounds(collider.bounds);
		}
	}

	void AddObjectToMap(Bounds bounds) {
		FillMapBounds(bounds, 1);
	}
	void AddObjectToMap(GameObject obj)
	{
		FillMapBounds(obj.GetComponent<BoxCollider>().bounds, 1);
	}


	void RemoveObjectFromMap(Bounds bounds) {
		FillMapBounds(bounds, 0);
	}
	void RemoveObjectFromMap(GameObject obj)
	{
		FillMapBounds(obj.GetComponent<BoxCollider>().bounds, 0);
	}

	#endregion

	Vector3 RoundVector(Vector3 toRound) {
		Vector3 position;
		position.x = Mathf.Round(toRound.x * 100f) / 100f;
		position.y = Mathf.Round(toRound.y * 100f) / 100f;
		position.z = Mathf.Round(toRound.z * 100f) / 100f;
		return position;
	}
	float timeStart;
	Vector3 posStart;
	void Update()
	{
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
						StructureElement hitObject = hit.transform.gameObject.GetComponent<StructureElement>();
						SetPivotObject(hitObject);

						AllowCameraMovements = false;
						CurrentlyPlacingObject = true;

						RemoveObjectFromMap(focusedObject.gameObject);
						focusedObject.gameObject.layer = 2;
						timeStart = Time.time;
						posStart = RoundVector(hit.point);
					}
				}
				else if (thisTouch.phase == TouchPhase.Ended)
				{
					if (CurrentlyPlacingObject)
					{
						focusedObject.gameObject.layer = 0;
						AllowCameraMovements = true;
						CurrentlyPlacingObject = false;
						posStart = Vector3.zero;
						timeStart = 0;
						AddObjectToMap(focusedObject.gameObject);

						actions.Add(string.Format("trx {0} {1} {2} {3} {4}",
						constructionObjects.IndexOf(focusedObject),
						Utils.DeserializeVector3(posStart),
						Utils.DeserializeVector3(focusedObject.positioner.position),
						Utils.DeserializeQuaternion(focusedObject.positioner.rotation),
						Utils.DeserializeQuaternion(focusedObject.positioner.rotation)));
					}
				}
				else
				{
					if (CurrentlyPlacingObject && Time.time - timeStart > 0.2)
					{
						Vector3 position = RoundVector(hit.point);
						Vector3 prevPosition = focusedObject.positioner.position;

						position.y = focusedObject.positioner.position.y;
						focusedObject.positioner.position = position;

						if(CheckBoundsOverlap(focusedObject.gameObject)) {
							focusedObject.positioner.position = prevPosition;
						}
					}
				}
			}
		}
		else
		{
			AllowCameraMovements = true;
			if (CurrentlyPlacingObject && focusedObject != null)
			{
				focusedObject.gameObject.layer = 0;
				AddObjectToMap(focusedObject.gameObject);
			}
			CurrentlyPlacingObject = false;
			posStart = Vector3.zero;
			timeStart = 0;
		}

		if (AllowCameraMovements)
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
