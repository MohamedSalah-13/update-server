
FROM ubuntu:22.04

# تثبيت الحزم الأساسية
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    mysql-server \
    curl \
    wget \
    && rm -rf /var/lib/apt/lists/*

# إنشاء مجلد العمل
WORKDIR /app

# نسخ ملفات المشروع
COPY src .

# إعطاء صلاحيات التشغيل لـ Maven Wrapper إذا كان موجوداً
RUN if [ -f "./mvnw" ]; then chmod +x ./mvnw; fi

# تعريض المنافذ
EXPOSE 8080 3306

# نقطة الدخول
ENTRYPOINT ["/bin/bash"]
