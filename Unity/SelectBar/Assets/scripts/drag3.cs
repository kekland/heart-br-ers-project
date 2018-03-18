using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(BoxCollider))]

public class drag3 : MonoBehaviour {
 private Vector3 screenPoint;
     private Vector3 offset;

	private float _lockedYPosition; 

     
	public bool objectGrabbed = false;
	Ray moRay; 
	public Transform moTransform; 
	public LayerMask whatIsMovableObject; 
	RaycastHit moHit; 

	// Use this for initialization
	void Start () {
		moHit = new RaycastHit ();
	}
	
	// Update is called once per frame
	void Update () {
		moRay = Camera.main.ScreenPointToRay (Input.mousePosition); 
	}

	void findAndGrabMoveObject(){}
	void findGroundBelowObject(){}
	void TraceMousePosRelToTheGround(){}}
