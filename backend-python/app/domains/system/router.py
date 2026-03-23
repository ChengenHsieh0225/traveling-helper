from fastapi import APIRouter, Depends

router = APIRouter()

@router.get("/ping")
async def ping():
    return {"status": "ok", "message": "Stayin' alive!"}